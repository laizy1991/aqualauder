package service.wx.dto.qrcode.limit;

public class LimitScene {
	private String scene_str = "";

	public LimitScene(String scene_str) {
		this.scene_str = new String(scene_str);
	}

	public String getScene_str() {
		return scene_str;
	}

	public void setScene_str(String scene_str) {
		this.scene_str = scene_str;
	}
	
}
