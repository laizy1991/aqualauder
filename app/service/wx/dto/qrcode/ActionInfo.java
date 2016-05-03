package service.wx.dto.qrcode;

public class ActionInfo {
	
	private Scene scene = null;
	
	public ActionInfo(Integer scene_id) {
		scene = new Scene(scene_id);
	}
	
	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

}
