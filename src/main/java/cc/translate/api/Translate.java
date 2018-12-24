package cc.translate.api;

import okhttp3.Dns;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cc.translate.Config;
import cc.translate.api.exception.IllegalTokenKeyException;
import cc.translate.api.exception.RetrieveTokenKeyFailedException;
import cc.translate.api.exception.TranslateFailedException;
import cc.translate.api.model.DictResult;
import cc.translate.api.model.TokenKey;
import cc.translate.api.model.TranslateResult;
import cc.translate.api.model.TranslateResult.Sentence;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Translate API Wrapper
 * 
 *  ligboy@gmail.com
 */
@SuppressWarnings("WeakerAccess")
public class Translate {

	public static final String SOURCE_LANG_AUTO = "auto";

	private static final String DEFAULT_BASE_URL = "https://translate.google.cn";

	private static TokenGenerator mTokenGenerator;
	private TranslateService mService;

    public static String md5(String data) throws NoSuchAlgorithmException {  
        MessageDigest md = MessageDigest.getInstance("MD5");  
        md.update(data.getBytes());  
        StringBuffer buf = new StringBuffer();  
        byte[] bits = md.digest();  
        for(int i=0;i<bits.length;i++){  
            int a = bits[i];  
            if(a<0) a+=256;  
            if(a<16) buf.append("0");  
            buf.append(Integer.toHexString(a));  
        }  
        return buf.toString();  
    }  
    
	Translate(Builder builder) {
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		HttpLoggingInterceptor.Level logLevel;
		switch (builder.logLevel) {
		case BASIC:
			logLevel = HttpLoggingInterceptor.Level.BASIC;
			break;
		case HEADERS:
			logLevel = HttpLoggingInterceptor.Level.HEADERS;
			break;
		case BODY:
			logLevel = HttpLoggingInterceptor.Level.BODY;
			break;
		case NONE:
		default:
			logLevel = HttpLoggingInterceptor.Level.NONE;
			break;
		}
		interceptor.setLevel(logLevel);
		OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(interceptor)
				.retryOnConnectionFailure(builder.retryOnConnectionFailure)
				.connectTimeout(builder.connectTimeout, builder.connectTimeoutTimeUnit)
				.writeTimeout(builder.writeTimeout, builder.writeTimeoutTimeUnit)
				.readTimeout(builder.readTimeout, builder.readTimeoutTimeUnit).proxy(builder.proxy);
		if (builder.proxySelector != null) {
			clientBuilder.proxySelector(builder.proxySelector);
		}
		if (builder.dns != null) {
			clientBuilder.dns(builder.dns);
		}
		Retrofit.Builder retrofitBuilder = new Retrofit.Builder().client(clientBuilder.build())
				.addConverterFactory(ScalarsConverterFactory.create())
				.addConverterFactory(TranslateConverterFactory.DEFAULT)
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(builder.baseUrl == null ? DEFAULT_BASE_URL : builder.baseUrl);
		mService = retrofitBuilder.build().create(TranslateService.class);
		// this.mTokenGenerator = builder.tokenGenerator;
	}

	/**
	 * Refresh the token key.
	 * 
	 * @throws RetrieveTokenKeyFailedException If failed throw this Exception.
	 * @throws IllegalTokenKeyException
	 */
	public void refreshTokenKey() throws RetrieveTokenKeyFailedException, IllegalTokenKeyException {
		if (mTokenGenerator != null && mTokenGenerator.isValid())
			return;

		Call<TokenKey> tokenKeyCall = mService.getTokenKey();
		Response<TokenKey> keyResponse;
		try {
			keyResponse = tokenKeyCall.execute();
		} catch (IOException e) {
			throw new RetrieveTokenKeyFailedException(e);
		}
		TokenKey tokenKey = keyResponse.body();
		if (keyResponse.isSuccessful() && tokenKey != null && tokenKey.getKey() != null) {
			mTokenGenerator = new TokenGenerator(tokenKey.getKey());
		} else {
			throw new RetrieveTokenKeyFailedException("Refresh token key failed.");
		}

		if (mTokenGenerator == null || mTokenGenerator.getTokenKey() == null) {
			throw new IllegalTokenKeyException("token key == null");
		}
	}

	public InputStream getAuidoUrl(String text, String tl)
			throws RetrieveTokenKeyFailedException, IllegalTokenKeyException, IOException {
		try {
			if (Config.cacheHistory) {
				File cacheFile = new File(System.getProperty("user.home") + File.separator + ".cc" + File.separator
						+ "caches" + File.separator + md5(text) + "-"+tl+".mp3");
				if (!cacheFile.exists() || !cacheFile.isFile()) {
					cacheFile.getParentFile().mkdirs();
					refreshTokenKey();
					Call<ResponseBody> response = mService.audio(text, mTokenGenerator.token(text), text.length(), tl);
					InputStream cacheStream = response.execute().body().byteStream();
					FileOutputStream os = new FileOutputStream(cacheFile);
					int read = 0;
					byte[] bytes = new byte[1024];

					while ((read = cacheStream.read(bytes)) != -1) {
						os.write(bytes, 0, read);
					}
					os.close();
					cacheStream.close();

				}
				return new FileInputStream(cacheFile);

			}

		}catch(Exception e) {
			e.printStackTrace();
		}
				refreshTokenKey();	
				Call<ResponseBody> response = mService.audio(text, mTokenGenerator.token(text), text.length(), tl);
		return response.execute().body().byteStream();

	}

	public void playTextAudio(String text, String tl)
			throws RetrieveTokenKeyFailedException, IllegalTokenKeyException, IOException, JavaLayerException {
		System.out.println(String.format("%s:%s", tl,text));
		InputStream is = getAuidoUrl(text, tl);
		BufferedInputStream buffer = new BufferedInputStream(is);
		Player player = new Player(buffer);
		player.play();
	}

	/**
	 * Translate text.
	 * 
	 * @param text   The text be translated.
	 * @param source The source Language. default "auto" - which means auto
	 *               detecting the source language.
	 * @param target The target Language.
	 * @return The translated result.
	 * @throws TranslateFailedException        exception if translation requesting
	 *                                         encountered errors.
	 * @throws IllegalTokenKeyException        exception if there something wrong
	 *                                         with the token key.
	 * @throws RetrieveTokenKeyFailedException
	 */
	@Nullable
	public TranslateResult translate(@NotNull String text, @Nullable String source, @NotNull String target)
			throws TranslateFailedException, IllegalTokenKeyException, RetrieveTokenKeyFailedException {
		if (source == null) {
			source = Translate.SOURCE_LANG_AUTO;
		}

		try {

			File cacheFile = new File(System.getProperty("user.home") + File.separator + ".cc" + File.separator
					+ "caches" + File.separator + md5(text) + "-"+target+".json");
			if(Config.cacheHistory) {

			if (cacheFile.exists() && cacheFile.isFile()) {
				
				return new Gson().fromJson(new InputStreamReader(new FileInputStream(cacheFile),StandardCharsets.UTF_8), TranslateResult.class);
			
			}else {
				cacheFile.getParentFile().mkdirs();
				
				TranslateResult result = null;
				if(text.matches("^[a-zA-Z]+$")){
					final Call<DictResult> resultCall = mService.translateDict(text);
					DictResult result2 = resultCall.execute().body();
					result = new TranslateResult();
					List<Sentence> sentenceList = new ArrayList<Sentence>(2);
					sentenceList.add(new Sentence());
					sentenceList.get(0).setSourceText(text);
					sentenceList.get(0).setTargetText(result2.getContent());
					result.setSentences(sentenceList);
				}
				
				
				if(result==null){
					refreshTokenKey();
					final Call<TranslateResult> resultCall = mService.translate(source, target, text, mTokenGenerator.token(text));
					result = resultCall.execute().body();
				}

				
				String jsonStr = new Gson().toJson(result);
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(cacheFile), StandardCharsets.UTF_8);
				writer.write(jsonStr);
				writer.close();
				return result;

			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}

		refreshTokenKey();

		final Call<TranslateResult> resultCall = mService.translate(source, target, text, mTokenGenerator.token(text));
		try {
			
			return resultCall.execute().body();
		} catch (IOException e) {
			throw new TranslateFailedException(e);
		}
	}


	
	/**
	 * Get the {@link TokenGenerator}.
	 * 
	 * @return The TokenGenerator.
	 */
	@Nullable
	public TokenGenerator getTokenGenerator() {
		return mTokenGenerator;
	}

	/**
	 * Set the {@link TokenGenerator}.
	 * 
	 * @param tokenGenerator The TokenGenerator.
	 */
	public void setTokenGenerator(@NotNull TokenGenerator tokenGenerator) {
		this.mTokenGenerator = tokenGenerator;
	}

	/**
	 * Build a new {@link Translate}.
	 */
	public static final class Builder {

		String baseUrl;
		TokenGenerator tokenGenerator;
		LogLevel logLevel;
		Proxy proxy;
		Dns dns;
		boolean retryOnConnectionFailure;
		long connectTimeout;
		TimeUnit connectTimeoutTimeUnit;
		long readTimeout;
		TimeUnit readTimeoutTimeUnit;
		long writeTimeout;
		TimeUnit writeTimeoutTimeUnit;
		ProxySelector proxySelector;

		public Builder() {
			logLevel = LogLevel.NONE;
			dns = Dns.SYSTEM;
			retryOnConnectionFailure = true;
			connectTimeout = 10_000;
			connectTimeoutTimeUnit = TimeUnit.MILLISECONDS;
			readTimeout = 10_000;
			readTimeoutTimeUnit = TimeUnit.MILLISECONDS;
			writeTimeout = 10_000;
			writeTimeoutTimeUnit = TimeUnit.MILLISECONDS;
		}

		/**
		 * Set the API base URL.
		 * 
		 * @param baseUrl The base URL of server.
		 * @return This {@link Builder}
		 */
		public Builder baseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
			return this;
		}

		/**
		 * Set the {@link TokenGenerator}.
		 * 
		 * @param tokenGenerator The TokenGenerator.
		 * @return This {@link Builder}
		 */
		public Builder tokenGenerator(@NotNull TokenGenerator tokenGenerator) {
			this.tokenGenerator = tokenGenerator;
			return this;
		}

		/**
		 * Set the level of logging.
		 * <p>
		 * Default {@link LogLevel#NONE}
		 * 
		 * @param logLevel The level of logging.
		 * @return This {@link Builder}
		 */
		public Builder logLevel(LogLevel logLevel) {
			this.logLevel = logLevel;
			return this;
		}

		/**
		 * Sets the DNS service used to lookup IP addresses for hostnames.
		 *
		 * <p>
		 * If unset, the {@link Dns#SYSTEM system-wide default} DNS will be used.
		 * 
		 * @param dns dns
		 * @return This {@link Builder}
		 */
		public Builder dns(Dns dns) {
			if (dns == null)
				throw new NullPointerException("dns == null");
			this.dns = dns;
			return this;
		}

		/**
		 * Set is retry on connection failure.
		 * 
		 * @param retryOnConnectionFailure retryOnConnectionFailure
		 * @return This {@link Builder}
		 */
		public Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
			this.retryOnConnectionFailure = retryOnConnectionFailure;
			return this;
		}

		/**
		 * Sets the default connect timeout for new connections. A value of 0 means no
		 * timeout, otherwise values must be between 1 and {@link Integer#MAX_VALUE}
		 * when converted to milliseconds.
		 * 
		 * @param timeout The timeout length.
		 * @param unit    The time unit.
		 * @return This {@link Builder}
		 */
		public Builder connectTimeout(long timeout, TimeUnit unit) {
			this.connectTimeout = timeout;
			this.connectTimeoutTimeUnit = unit;
			return this;
		}

		/**
		 * Sets the default read timeout for new connections. A value of 0 means no
		 * timeout, otherwise values must be between 1 and {@link Integer#MAX_VALUE}
		 * when converted to milliseconds.
		 * 
		 * @param timeout The timeout length.
		 * @param unit    The time unit.
		 * @return This {@link Builder}
		 */
		public Builder readTimeout(long timeout, TimeUnit unit) {
			this.readTimeout = timeout;
			this.readTimeoutTimeUnit = unit;
			return this;
		}

		/**
		 * Sets the default write timeout for new connections. A value of 0 means no
		 * timeout, otherwise values must be between 1 and {@link Integer#MAX_VALUE}
		 * when converted to milliseconds.
		 * 
		 * @param timeout The timeout length.
		 * @param unit    The time unit.
		 * @return This {@link Builder}
		 */
		public Builder writeTimeout(long timeout, TimeUnit unit) {
			this.writeTimeout = timeout;
			this.writeTimeoutTimeUnit = unit;
			return this;
		}

		/**
		 * Sets the HTTP proxy that will be used by connections created by this client.
		 * This takes precedence over {@link #proxySelector}, which is only honored when
		 * this proxy is null (which it is by default). To disable proxy use completely,
		 * call {@code setProxy(Proxy.NO_PROXY)}.
		 * 
		 * @param proxy proxy
		 * @return This {@link Builder}
		 */
		public Builder proxy(Proxy proxy) {
			this.proxy = proxy;
			return this;
		}

		/**
		 * Sets the proxy selection policy to be used if no {@link #proxy proxy} is
		 * specified explicitly. The proxy selector may return multiple proxies; in that
		 * case they will be tried in sequence until a successful connection is
		 * established.
		 *
		 * <p>
		 * If unset, the {@link ProxySelector#getDefault() system-wide default} proxy
		 * selector will be used.
		 * 
		 * @param proxySelector The proxy selection policy.
		 * @return This {@link Builder}
		 */
		public Builder proxySelector(ProxySelector proxySelector) {
			this.proxySelector = proxySelector;
			return this;
		}

		/**
		 * Create the {@link Translate} instance using the configured values.
		 * 
		 * @return This {@link Builder}
		 */
		public Translate build() {
			return new Translate(this);
		}
	}

	public enum LogLevel {
		/** No logs. */
		NONE,
		/**
		 * Logs request and response lines.
		 *
		 * <p>
		 * Example:
		 * 
		 * <pre>
		 * {@code
		 * --> POST /greeting http/1.1 (3-byte body)
		 *
		 * <-- 200 OK (22ms, 6-byte body)
		 * }
		 * </pre>
		 */
		BASIC,
		/**
		 * Logs request and response lines and their respective headers.
		 *
		 * <p>
		 * Example:
		 * 
		 * <pre>
		 * {@code
		 * --> POST /greeting http/1.1
		 * Host: example.com
		 * Content-Type: plain/text
		 * Content-Length: 3
		 * --> END POST
		 *
		 * <-- 200 OK (22ms)
		 * Content-Type: plain/text
		 * Content-Length: 6
		 * <-- END HTTP
		 * }
		 * </pre>
		 */
		HEADERS,
		/**
		 * Logs request and response lines and their respective headers and bodies (if
		 * present).
		 *
		 * <p>
		 * Example:
		 * 
		 * <pre>
		 * {@code
		 * --> POST /greeting http/1.1
		 * Host: example.com
		 * Content-Type: plain/text
		 * Content-Length: 3
		 *
		 * Hi?
		 * --> END GET
		 *
		 * <-- 200 OK (22ms)
		 * Content-Type: plain/text
		 * Content-Length: 6
		 *
		 * Hello!
		 * <-- END HTTP
		 * }
		 * </pre>
		 */
		BODY
	}
}
