import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

public class CustomBurp implements BurpExtension{

    @Override
    public void initialize(MontoyaApi api) {
        api.extension().setName("Evidence Creator");

        api.userInterface().registerContextMenuItemsProvider(new CreateEvidenceContext(api));
    }
}
