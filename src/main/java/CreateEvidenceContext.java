
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ToolType;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;

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

            HttpRequestResponse requestResponse = event.messageEditorRequestResponse().isPresent() ? event.messageEditorRequestResponse().get().requestResponse() : event.selectedRequestResponses().getFirst();

            createEvidenceItem.addActionListener(e -> {
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
                    fileChooser.setFileFilter(filter);
                    fileChooser.setDialogTitle("Save As");
                    fileChooser.setSelectedFile(new File("_NoSignature_200_Normal.txt"));
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.documents") + ".txt"));

                    int userSelection = fileChooser.showSaveDialog(null);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {

                        File fileToSave = fileChooser.getSelectedFile();

                        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileToSave), StandardCharsets.UTF_8);
                             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

                            String httpEvidenceDataRequest = new String(requestResponse.request().toByteArray().getBytes(), StandardCharsets.UTF_8);
                            String httpEvidenceDataResponse = new String(requestResponse.response().toByteArray().getBytes(), StandardCharsets.UTF_8);

                            bufferedWriter.write(httpEvidenceDataRequest + "\r\n\r\n--------------------------------------------------\r\n\r\n" + httpEvidenceDataResponse);
                            bufferedWriter.close();
                            api.logging().logToOutput("File saved at: " + fileToSave.getAbsolutePath());

                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    } else {
                        api.logging().logToOutput("Save command cancelled");
                    }
            });

            menuItemList.add(createEvidenceItem);

            return menuItemList;
        }

        return null;
    }
}
