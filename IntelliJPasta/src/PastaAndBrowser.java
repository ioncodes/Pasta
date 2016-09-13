import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by LucaMarcelli on 12.09.2016.
 */
public class PastaAndBrowser extends AnAction {
    public PastaAndBrowser() {

        super("Open in Browser and copy link");
    }

    public void actionPerformed(AnActionEvent event) {
        try {
            User.logIn();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        String source = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument().getText();
        try {
            String url = Helpers.sendPost("http://www.ioncodes.com/Pasta/create.php?token=" + User.getAccessToken(), "source=" + source, true);
            StringSelection selection = new StringSelection(url);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            Helpers.openWebpage(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
