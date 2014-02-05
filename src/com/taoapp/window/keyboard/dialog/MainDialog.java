package com.taoapp.window.keyboard.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jnativehook.NativeHookException;

import com.tao.common.util.ArrayUtil;
import com.tao.swt.hook.Hook;
import com.taoapp.window.keyboard.listener.GlobalKeyListener;
import com.taoapp.window.keyboard.listener.WindowListener;

public class MainDialog {

	protected Shell shell;
	protected String shell_image = "/com/taoapp/window/keyboard/res/shell.png";
	protected String keyboard_body = "/com/taoapp/window/keyboard/res/keyboard.png";

	protected Composite top_comp;
	protected Composite top_comp_part1;
	protected Composite top_comp_part2;
	protected Composite bottom_comp;
	protected Composite key_comp;
	protected Text text;
	protected Canvas keyCanvas;

	protected Label[] label_title_array = new Label[77];
	protected Label[] label_value_array = new Label[77];
	protected String[] titles = new String[]{"F1", "F2", "F3", "F4", "F5",
			"F6", "F7", "F8", "F9", "F10", "F11", "F12", "1", "2", "3", "4",
			"5", "6", "7", "8", "9", "0", "q(Q)", "w(W)", "e(E)", "r(R)",
			"t(T)", "y(Y)", "u(U)", "i(I)", "o(O)", "p(P)", "a(A)", "s(S)",
			"d(D)", "f(F)", "g(G)", "h(H)", "j(J)", "k(K)", "l(L)", "z(Z)",
			"x(X)", "c(C)", "v(V)", "b(B)", "n(N)", "m(M)", "Esc", "Home",
			"~(`)", "-(_)", "+(=)", "BackSpace", "Tab", "{([)", "}(])",
			"\\(|)", "Caps", ":(;)", "'(\")", ",(<)", ".(>)", "?(/)", "Shift",
			"Ctrl", "Windows", "Alt", "Space", "Up", "Left", "Down", "Right"};

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainDialog window = new MainDialog();
			window.open();
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		createListeners();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.MIN);
		shell.setImage(SWTResourceManager.getImage(MainDialog.class,
				shell_image));
		shell.setSize(1069, 705);
		shell.setText("键盘监控器");
		shell.setLayout(new GridLayout(1, false));

		top_comp = new Composite(shell, SWT.NONE);
		top_comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		top_comp.setLayout(new GridLayout(2, false));

		top_comp_part1 = new Composite(top_comp, SWT.NONE);
		top_comp_part1.setLayoutData(new GridData(GridData.FILL_BOTH));
		top_comp_part1.setLayout(new FillLayout());

		keyCanvas = new Canvas(top_comp_part1, SWT.NONE);
		keyCanvas.setBackgroundImage(SWTResourceManager.getImage(
				MainDialog.class, keyboard_body));

		top_comp_part2 = new Composite(top_comp, SWT.NONE);
		GridData top_comp_part2_gridData = new GridData(GridData.FILL_VERTICAL);
		top_comp_part2_gridData.widthHint = 200;
		top_comp_part2.setLayoutData(top_comp_part2_gridData);
		top_comp_part2.setLayout(new GridLayout(1, false));

		Label lblLog = new Label(top_comp_part2, SWT.NONE);
		lblLog.setText("日志：");
		GridData lblLog_gridData = new GridData(GridData.FILL_HORIZONTAL);
		lblLog_gridData.heightHint = 20;
		lblLog.setLayoutData(lblLog_gridData);

		text = new Text(top_comp_part2, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));

		bottom_comp = new Composite(shell, SWT.NONE);
		GridData right_GridData = new GridData(GridData.FILL_HORIZONTAL);
		right_GridData.heightHint = 300;
		bottom_comp.setLayoutData(right_GridData);
		bottom_comp.setLayout(new GridLayout(1, false));

		Label lblStatistics = new Label(bottom_comp, SWT.NONE);
		lblStatistics.setText("统计(符号、次数)：");
		GridData lblStatistics_gridData = new GridData(GridData.FILL_HORIZONTAL);
		lblStatistics_gridData.heightHint = 20;
		lblStatistics.setLayoutData(lblStatistics_gridData);

		key_comp = new Composite(bottom_comp, SWT.BORDER | SWT.SEPARATOR);
		key_comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		key_comp.setLayout(new GridLayout(22, false));

		ArrayUtil.sort(titles);
		for (int i = 0; i < titles.length; i++) {
			label_title_array[i] = new Label(key_comp, SWT.NONE);
			label_title_array[i].setText(titles[i]);
			GridData label_title_gridData = new GridData(GridData.FILL_BOTH);
			label_title_gridData.horizontalAlignment = SWT.LEFT;
			label_title_gridData.verticalAlignment = SWT.CENTER;
			label_title_array[i].setLayoutData(label_title_gridData);

			label_value_array[i] = new Label(key_comp, SWT.NONE);
			label_value_array[i].setText("0");
			GridData label_value_gridData = new GridData(GridData.FILL_VERTICAL);
			label_value_gridData.widthHint = 40;
			label_value_gridData.horizontalAlignment = SWT.LEFT;
			label_value_gridData.verticalAlignment = SWT.CENTER;
			label_value_array[i].setLayoutData(label_value_gridData);
		}

	}
	protected void createListeners() {
		shell.addShellListener(new WindowListener());

		try {
			Hook.register();
			Hook.addKeyListener(new GlobalKeyListener(this));
		} catch (NativeHookException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public Text getText() {
		return text;
	}

	public Label[] getLabel_value_array() {
		return label_value_array;
	}

	public String[] getTitles() {
		return titles;
	}

	public Canvas getKeyCanvas() {
		return keyCanvas;
	}
}
