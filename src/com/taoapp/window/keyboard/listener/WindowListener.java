package com.taoapp.window.keyboard.listener;

import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

import com.tao.swt.hook.Hook;

public class WindowListener extends ShellAdapter {

	public WindowListener() {
	}
	@Override
	public void shellClosed(ShellEvent shellevent) {
		try {
			Hook.unregister();
		} catch (Exception ex) {
			System.err.println("There was a problem removing the native hook.");
			System.err.println(ex.getMessage());
		}
		shellevent.doit = true;
	}
}
