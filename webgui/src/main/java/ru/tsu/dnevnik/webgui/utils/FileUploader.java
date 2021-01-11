package ru.tsu.dnevnik.webgui.utils;

import com.vaadin.server.FileResource;
import com.vaadin.ui.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Alexey on 10.01.2015.
 */
public class FileUploader extends CustomComponent
        implements Upload.SucceededListener,
        Upload.FailedListener,
        Upload.Receiver {
    final HorizontalLayout root;         // Root element for contained components.
    final HorizontalLayout imagePanel;   // Panel that contains the uploaded image.
    File file;         // File to write to.
    public FileUploader() {
        root = new HorizontalLayout();
        setCompositionRoot(root);
        // Create the Upload component.
        final Upload upload =
                new Upload("Выберите файл для загрузки", this);
        // Use a custom button caption instead of plain "Upload".
        upload.setButtonCaption("Загрузить");
        // Listen for events regarding the success of upload.
        upload.addListener((Upload.SucceededListener) this);
        upload.addListener((Upload.FailedListener) this);
        root.addComponent(upload);
        root.addComponent(new Label("Click 'Browse' to "+
                "select a file and then click 'Upload'."));
        // Create a panel for displaying the uploaded image.
        imagePanel = new HorizontalLayout();
        imagePanel.addComponent(
                new Label("No image uploaded yet"));
        root.addComponent(imagePanel);
    }
    // Callback method to begin receiving the upload.
    public OutputStream receiveUpload(String filename,
                                      String MIMEType) {
        FileOutputStream fos = null; // Output stream to write to
        file = new File(filename);
        try {
            // Open the file for writing.
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            // Error while opening the file. Not reported here.
            e.printStackTrace();
            return null;
        }
        return fos; // Return the output stream to write to
    }
    // This is called if the upload is finished.
    public void uploadSucceeded(Upload.SucceededEvent event) {
        // Log the upload on screen.
        root.addComponent(new Label("File " + event.getFilename()
                + " of type '" + event.getMIMEType()
                + "' uploaded."));
        // Display the uploaded file in the image panel.
        final FileResource imageResource =
                new FileResource(file);
        imagePanel.removeAllComponents();
        imagePanel.addComponent(new Embedded("", imageResource));
    }
    // This is called if the upload fails.
    public void uploadFailed(Upload.FailedEvent event) {
        // Log the failure on screen.
        root.addComponent(new Label("Uploading "
                + event.getFilename() + " of type '"
                + event.getMIMEType() + "' failed."));
    }


}
