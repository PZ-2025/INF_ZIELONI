package com.example.obiwankenobi;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ReportControllerTest {

    private ReportController controller;
    private TestActionEvent testEvent;
    private Stage testStage;

    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        controller = new ReportController();

        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            testStage = new Stage();
            Button testButton = new Button();
            VBox root = new VBox(testButton);
            Scene scene = new Scene(root, 200, 200);
            testStage.setScene(scene);

            testEvent = new TestActionEvent(testButton);
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void shouldInitializeControllerWithoutErrors() {
        assertDoesNotThrow(() -> controller.initialize(null, null));
    }

    @Test
    void shouldShowUserReportAndHandleIOException() {
        assertDoesNotThrow(() -> {
            try {
                controller.userReportShow(testEvent);
            } catch (IOException e) {
                assertTrue(e.getMessage().contains("report1.fxml") ||
                        e.getMessage().contains("Location is not set") ||
                        e.getMessage().contains("resources"));
            }
        });
    }

    @Test
    void shouldCloseReportsWindow() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            testStage.show();
            assertTrue(testStage.isShowing());

            controller.reportsClose(testEvent);

            assertFalse(testStage.isShowing());
            latch.countDown();
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    void shouldThrowExceptionOnNullEvent() {
        assertThrows(NullPointerException.class, () -> controller.userReportShow(null));
    }

    private static class TestActionEvent extends ActionEvent {
        private final Node source;

        public TestActionEvent(Node source) {
            super(source, null);
            this.source = source;
        }

        @Override
        public Node getSource() {
            return source;
        }
    }
}
