package service.wx.dto.qrcode.limit;


public class LimitActionInfo {
	
	private LimitScene scene = null;
	
	public LimitActionInfo(String scene_str) {
		this.scene = new LimitScene(scene_str);
	}
	
	public LimitScene getScene() {
		return scene;
	}

	public void setScene(LimitScene scene) {
		this.scene = scene;
	}

}
