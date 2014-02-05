package com.taoapp.window.keyboard.listener;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jnativehook.keyboard.NativeKeyEvent;

import com.tao.common.codemap.KeyCodeMap;
import com.tao.common.util.ArrayUtil;
import com.tao.common.util.DateUtil;
import com.tao.swt.hook.listener.NativeKeyAdapter;
import com.taoapp.window.keyboard.dialog.MainDialog;

public class GlobalKeyListener extends NativeKeyAdapter {

	private MainDialog dialog;
	private Text text;
	private int keyCode;
	private Label[] label_value_array;
	private String[] titles;

	public GlobalKeyListener(Object obj) {
		dialog = (MainDialog) obj;
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		keyCode = e.getKeyCode();
		text = dialog.getText();
		label_value_array = dialog.getLabel_value_array();
		titles = dialog.getTitles();
		setLabelValue();
	}

	public void setLabelValue() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				// String keyText = NativeKeyEvent.getKeyText(keyCode);
				String keyText = KeyCodeMap.keyText(keyCode);
				String content = DateUtil.getCurrentTime() + " Äú°´ÏÂÁË:["
						+ keyText + "]\n\r";
				text.append(content);

				int index = ArrayUtil.find(titles, keyText);

				if (index >= 0) {
					int num = Integer.parseInt(label_value_array[index]
							.getText());
					label_value_array[index].setText(String.valueOf(num + 1));
				}
			}
		});
	}
}