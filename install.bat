commons-daemon\prunsrv //IS//CCTranslate --DisplayName="CC Translate" --Description="CCTranslate"^
     --Install="%cd%\commons-daemon\prunsrv.exe" --Jvm="%cd%\jre1.8.0_181\bin\client\jvm.dll" --StartMode=jvm --StopMode=jvm^
     --Startup=auto --StartClass=cc.translate.CcTranslate --StopClass=cc.translate.CcTranslate^
     --StartParams=start --StopParams=stop --StartMethod=windowsService --StopMethod=windowsService^
     --Classpath="%cd%\cctranslate.jar" --LogLevel=DEBUG^ --LogPath="%cd%\logs" --LogPrefix=procrun.log^
     --StdOutput="%cd%\logs\stdout.log" --StdError="%cd%\logs\stderr.log"
     
     
commons-daemon\prunsrv //ES//CCTranslate