package com.dina.genasoft.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Component;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

@Component
public class UploadFileEmail extends CustomComponent {
    private static final long serialVersionUID = -4292553844521293140L;

    private String            docsPathTemp;
    private String            path;

    public void init(String context, String path) {
        VerticalLayout layout = new VerticalLayout();

        docsPathTemp = path;
        if ("basic".equals(context))
            basic(layout);
        else if ("advanced".equals(context))
            advanced(layout);
        else layout.addComponent(new Label("Invalid context: " + context));

        setCompositionRoot(layout);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String pth) {
        this.path = pth;
    }

    void basic(VerticalLayout layout) {
        // BEGIN-EXAMPLE: component.upload.basic
        // Show uploaded file in this placeholder
        final Image image = new Image("Archivo adjuntado correctamente");
        image.setVisible(false);

        // Implement both receiver that saves upload in a file and
        // listener for successful upload
        class FileReceiver implements Receiver ,SucceededListener {
            private static final long serialVersionUID = -1276759102490466761L;

            public File               file;

            public OutputStream receiveUpload(String filename, String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Stream to write to
                try {
                    // Open the file for writing.
                    if (filename == null || filename.trim().isEmpty()) {
                        new Notification("Se debe eleccionar el fichero previamente pulsando el botón 'Examinar'", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                    } else {
                        file = new File(docsPathTemp + "/PARTIDAS/" + filename);
                        path = docsPathTemp + "/PARTIDAS/" + filename;
                        if (!file.isDirectory()) {
                            if (file.exists()) {
                                file.delete();
                            }
                            fos = new FileOutputStream(file);
                            new Notification("Fichero seleccionado correctamente", Notification.Type.HUMANIZED_MESSAGE).show(Page.getCurrent());
                        } else {
                            new Notification("Se debe eleccionar el fichero previamente pulsando el botón 'Examinar'", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                        }
                    }

                } catch (final java.io.FileNotFoundException e) {
                    if (filename == null || filename.isEmpty()) {
                        new Notification("Se debe eleccionar el fichero previamente pulsando el botón 'Examinar'", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                    } else {
                        file.mkdir();
                        new Notification("No se puede abrir el fichero", e.getMessage(), Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());
                    }
                    return null;
                }
                return fos; // Return the output stream to write to
            }

            public void uploadSucceeded(SucceededEvent event) {
                // Show the uploaded file in the image viewer
                image.setVisible(true);
                image.setSource(new FileResource(file));
                image.setWidth(1, Sizeable.Unit.PIXELS);
                image.setHeight(1, Sizeable.Unit.PIXELS);
            }
        };
        FileReceiver receiver = new FileReceiver();

        // Create the upload with a caption and set receiver later
        final Upload upload = new Upload("Adjuntar fichero", receiver);

        upload.setButtonCaption("Adjuntar fichero");
        upload.addSucceededListener(receiver);

        // Prevent too big downloads
        final long UPLOAD_LIMIT = 100000000l;
        upload.addStartedListener(new StartedListener() {
            private static final long serialVersionUID = 4728847902678459488L;

            @Override
            public void uploadStarted(StartedEvent event) {
                if (event.getContentLength() > UPLOAD_LIMIT) {
                    Notification.show("Archivo demasiado grande, Máx. 10 MB", Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });

        // Check the size also during progress 
        upload.addProgressListener(new ProgressListener() {
            private static final long serialVersionUID = 8587352676703174995L;

            @Override
            public void updateProgress(long readBytes, long contentLength) {
                if (readBytes > UPLOAD_LIMIT) {
                    Notification.show("Archivo demasiado grande, Máx. 10 MB", Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });

        // Put the components in a panel
        Panel panel = new Panel("Adjuntar fichero");
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setMargin(true);
        panelContent.addComponents(upload, image);
        panel.setContent(panelContent);
        // END-EXAMPLE: component.upload.basic

        // Create uploads directory
        File uploads = new File(docsPathTemp + "/PARTIDAS/");
        if (!uploads.exists() && !uploads.mkdir())
            layout.addComponent(new Label("ERROR: No se ha podido crear el directorio temporal para subir archivos"));

        ((VerticalLayout) panel.getContent()).setSpacing(true);
        panel.setWidth("-1");
        layout.addComponent(panel);
    }

    void advanced(VerticalLayout layout) {
        // BEGIN-EXAMPLE: component.upload.advanced
        class UploadBox extends CustomComponent implements Receiver ,ProgressListener ,FailedListener ,SucceededListener {
            private static final long serialVersionUID = -46336015006190050L;

            // Put upload in this memory buffer that grows automatically
            ByteArrayOutputStream     os               = new ByteArrayOutputStream(10240);

            // Name of the uploaded file
            String                    filename;

            ProgressBar               progress         = new ProgressBar(0.0f);

            // Show uploaded file in this placeholder
            Image                     image            = new Image("Archivo adjuntado");

            public UploadBox() {
                // Create the upload component and handle all its events
                Upload upload = new Upload("Adjuntar fichero", null);
                upload.setReceiver(this);
                upload.addProgressListener(this);
                upload.setButtonCaption("Seleccionar fichero");
                upload.setCaption("");
                upload.addFailedListener(this);
                upload.addSucceededListener(this);

                // Put the upload and image display in a panel
                Panel panel = new Panel("");
                //panel.setWidth("400px");
                //panel.setWidth("400px");
                VerticalLayout panelContent = new VerticalLayout();
                panelContent.setSpacing(true);
                panel.setContent(panelContent);
                panelContent.addComponent(upload);
                panelContent.addComponent(progress);
                panelContent.addComponent(image);

                progress.setVisible(false);
                image.setVisible(false);

                setCompositionRoot(panel);
            }

            public OutputStream receiveUpload(String filename, String mimeType) {

                this.filename = filename;

                os.reset(); // Needed to allow re-uploading
                return os;
            }

            @Override
            public void updateProgress(long readBytes, long contentLength) {
                progress.setVisible(true);
                if (contentLength == -1)
                    progress.setIndeterminate(true);
                else {
                    progress.setIndeterminate(false);
                    progress.setValue(((float) readBytes) / ((float) contentLength));
                }
            }

            public void uploadSucceeded(SucceededEvent event) {
                image.setVisible(true);
                image.setCaption("Arcihvo adjuntado correctamente " + filename);

                // Display the image as a stream resource from
                // the memory buffer
                StreamSource source = new StreamSource() {
                    private static final long serialVersionUID = -4905654404647215809L;

                    public InputStream getStream() {
                        return new ByteArrayInputStream(os.toByteArray());
                    }
                };

                if (image.getSource() == null)
                    // Create a new stream resource
                    image.setSource(new StreamResource(source, filename));
                else { // Reuse the old resource
                    StreamResource resource = (StreamResource) image.getSource();
                    resource.setStreamSource(source);
                    resource.setFilename(filename);
                }

                image.markAsDirty();
            }

            @Override
            public void uploadFailed(FailedEvent event) {
                Notification.show("Error al adjuntar el archivo", Notification.Type.ERROR_MESSAGE);
            }
        }

        UploadBox uploadbox = new UploadBox();
        layout.addComponent(uploadbox);
        // END-EXAMPLE: component.upload.advanced
    }
}