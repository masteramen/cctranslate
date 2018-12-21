package cc.translate;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class RoundedPanel3 {
	/**
	 * Stroke size. it is recommended to set it to 1 for better view
	 */
	public int strokeSize;
	/**
	 * Color of shadow
	 */
	public Color shadowColor;
	/**
	 * Sets if it drops shadow
	 */
	public boolean shady;
	/**
	 * Sets if it has an High Quality view
	 */
	public boolean highQuality;
	/**
	 * Double values for Horizontal and Vertical radius of corner arcs
	 */
	public Dimension arcs;
	/**
	 * Distance between shadow border and opaque panel border
	 */
	public int shadowGap;
	/**
	 * The offset of shadow.
	 */
	public int shadowOffset;
	/**
	 * The transparency value of shadow. ( 0 - 255)
	 */
	public int shadowAlpha;
	public int width;
	public int height;
	public BufferedImage image;
	public BufferedImage roundedImage;

	public RoundedPanel3(int strokeSize, Color shadowColor, boolean shady, boolean highQuality, Dimension arcs,
			int shadowGap, int shadowOffset, int shadowAlpha, int width, int height) {
		this.strokeSize = strokeSize;
		this.shadowColor = shadowColor;
		this.shady = shady;
		this.highQuality = highQuality;
		this.arcs = arcs;
		this.shadowGap = shadowGap;
		this.shadowOffset = shadowOffset;
		this.shadowAlpha = shadowAlpha;
		this.width = width;
		this.height = height;
	}
}