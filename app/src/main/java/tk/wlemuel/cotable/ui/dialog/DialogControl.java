package tk.wlemuel.cotable.ui.dialog;

/**
 * Created by stevelemuel on 4/17/15.
 */
public interface DialogControl {

    public abstract void hideWaitDialog();

    public abstract WaitDialog showWaitDialog();

    public abstract WaitDialog showWaitDialog(int resId);

    public abstract WaitDialog showWaitDialog(String text);
}
