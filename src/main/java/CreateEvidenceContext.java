
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ToolType;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateEvidenceContext implements ContextMenuItemsProvider{

    private final MontoyaApi api;

    public CreateEvidenceContext(MontoyaApi api)
    {
        this.api = api;
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event)
    {
        if (event.isFromTool(ToolType.PROXY, ToolType.TARGET, ToolType.LOGGER))
        {
            List<Component> menuItemList = new ArrayList<>();

            JMenuItem createEvidenceItem = new JMenuItem("Create Evidence");

            HttpRequestResponse requestResponse = event.messageEditorRequestResponse().isPresent() ? event.messageEditorRequestResponse().get().requestResponse() : event.selectedRequestResponses().get(0);

            createEvidenceItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Save As");
                        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

                        int userSelection = fileChooser.showSaveDialog(null);

                        if (userSelection == JFileChooser.APPROVE_OPTION) {

                            File fileToSave = fileChooser.getSelectedFile();

                            try (FileWriter writer = new FileWriter(fileToSave)) {
                                writer.write(requestResponse.request().toString() + "\n--------------------------------------------------\n" + requestResponse.response().toString());
                                writer.close();
                                api.logging().logToOutput(requestResponse.request().toString() + "\n--------------------------------------------------\n" + requestResponse.response().toString());
                                api.logging().logToOutput("File saved at: " + fileToSave.getAbsolutePath());

                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        } else {
                            api.logging().logToOutput("Save command cancelled");
                        }
                }
            });

            menuItemList.add(createEvidenceItem);

            return menuItemList;
        }

        return null;
    }
}
