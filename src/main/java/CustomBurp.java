import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.menu.BasicMenuItem;
import burp.api.montoya.ui.menu.Menu;
import burp.api.montoya.ui.menu.MenuItem;

public class CustomBurp implements BurpExtension{

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("Evidence Creator");

        api.userInterface().registerContextMenuItemsProvider(new CreateEvidenceContext(api));
    }
}
