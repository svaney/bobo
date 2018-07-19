package com.bobo.gmargiani.bobo.model;

/**
 * Created by gmargiani on 1/30/2018.
 */

public class AppVersion {
    private String _id;
    private String dialogText;
    private boolean showDialog;
    private String title;
    private String okButtonLink;
    private boolean showCancelButton;
    private boolean dismissAfterOK;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDialogText() {
        return dialogText;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    public boolean showDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOkButtonLink() {
        return okButtonLink;
    }

    public void setOkButtonLink(String okButtonLink) {
        this.okButtonLink = okButtonLink;
    }

    public boolean showCancelButton() {
        return showCancelButton;
    }

    public void setShowCancelButton(boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
    }

    public boolean dismissAfterOK() {
        return dismissAfterOK;
    }

    public void setDismissAfterOK(boolean dismissAfterOK) {
        this.dismissAfterOK = dismissAfterOK;
    }
}
