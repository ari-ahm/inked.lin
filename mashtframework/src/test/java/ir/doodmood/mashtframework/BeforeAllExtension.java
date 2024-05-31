package ir.doodmood.mashtframework;

import ir.doodmood.mashtframework.web.MashtApplication;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.LinkedList;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class BeforeAllExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {
    private static boolean started = false;
    private static MashtApplication application;
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest").withExposedPorts(5432).withDatabaseName("inkedlin");

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {
            started = true;

            LinkedList<String> mmd = new LinkedList<>();
            mmd.add("5432:5432");
            postgres.setPortBindings(mmd);
            postgres.start();

            application = MashtApplication.run(BeforeAllExtension.class);

            context.getRoot().getStore(GLOBAL).put("beforeallextensionuniquename", this);
        }
    }

    @Override
    public void close() {
        application.close();
        postgres.stop();
    }
}
