package service.wx.dto.qrcode.tmp;

public class TmpScene {
	private Integer scene_id = 0;

	public TmpScene(Integer scene_id) {
		this.scene_id = new Integer(scene_id);
	}
	
	public Integer getScene_id() {
		return scene_id;
	}

	public void setScene_id(Integer scene_id) {
		this.scene_id = scene_id;
	}
}
