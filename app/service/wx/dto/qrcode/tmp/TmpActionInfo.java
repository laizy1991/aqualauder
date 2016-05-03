package service.wx.dto.qrcode.tmp;


public class TmpActionInfo {
	
	private TmpScene scene = null;
	
	public TmpActionInfo(Integer scene_id) {
		scene = new TmpScene(scene_id);
	}
	
	public TmpScene getScene() {
		return scene;
	}

	public void setScene(TmpScene scene) {
		this.scene = scene;
	}

}
