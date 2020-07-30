package com.homw.gui.d2.swing;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

import cn.hutool.core.lang.Assert;

import java.util.*;

/**
 * @description Applet Window
 * @author Hom
 * @version 1.0
 * @since 2020-07-29
 */
public class AppletFrame extends JFrame implements AppletStub, AppletContext {

	private static final long serialVersionUID = -3481000474954768808L;
	
	private String name;
    private Applet applet;
    private Label label = null;
    private Dimension appletSize;

    private static final String PARAM_PROP_PREFIX = "applet.param.";

    public AppletFrame(Applet applet, int width, int height) {
        build(applet, null, width, height);
    }

    public AppletFrame(Applet applet, String[] args, int width, int height) {
        build(applet, args, width, height);
    }

    private void build(Applet applet, String[] args, int width, int height) {
        Assert.notNull(applet, "applet parameter must be not null");

        this.applet = applet;
        this.appletSize = applet.getSize();
        this.name = applet.getClass().getName();

        applet.setStub(this);
        setTitle(name);

        Properties props = getProperties();
        if (args != null) {
            parseArgs(args, props);
        }

        // layout components
        getContentPane().add(applet, BorderLayout.CENTER);

        // setup size
        updateSize(width, height);

        // bind close listener
        addCloseListener();
        
        setVisible(true);
    }

    private void addCloseListener() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent winEvent) {
                if (AppletFrame.this.applet != null) {
                    AppletFrame.this.applet.destroy();
                }

                setVisible(false);
                try {
                    dispose();
                } catch (IllegalStateException e) {
                }

                if (checkExitPermission()) {
                    System.exit(0);
                }
            }
        });
    }

    private boolean checkExitPermission() {
        SecurityManager sm = System.getSecurityManager();
        boolean doExit = true;
        if (sm != null) {
            try {
                sm.checkExit(0);
            } catch (SecurityException e) {
                doExit = false;
            }
        }
        return doExit;
    }

    private Properties getProperties() {
        Properties props = System.getProperties();
        props.put("browser", this.name);
        props.put("browser.version", "1.0.0");
        props.put("browser.vendor", "Homw");
        props.put("browser.vendor.url", "https://github.com/homw188/homw-framework");
        return props;
    }

    private void updateSize(int width, int height) {
        pack();
        validate();
        applet.setSize(width, height);
    }

    private static void parseArgs(String[] args, Properties props) {
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            int ind = arg.indexOf('=');
            if (ind == -1) {
                props.put(PARAM_PROP_PREFIX + arg.toLowerCase(), "");
            } else {
                props.put(PARAM_PROP_PREFIX + arg.substring(0, ind).toLowerCase(),
                        arg.substring(ind + 1));
            }
        }
    }

    public void start() {
        showStatus(name + " initializing...");
        applet.init();
        validate();

        showStatus(name + " starting...");
        applet.start();
        validate();

        showStatus(name + " running...");
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        String dir = System.getProperty("user.dir");
        String urlDir = dir.replace(File.separatorChar, '/');
        try {
            return new URL("file:" + urlDir + "/");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    public URL getCodeBase() {
        String path = System.getProperty("java.class.path");
        Enumeration<Object> st = new StringTokenizer(path, ":");
        while (st.hasMoreElements()) {
            String dir = (String) st.nextElement();
            String filename = dir + File.separatorChar + name + ".class";
            File file = new File(filename);
            if (file.exists()) {
                String urlDir = dir.replace(File.separatorChar, '/');
                try {
                    return new URL("file:" + urlDir + "/");
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public String getParameter(String name) {
        return System.getProperty(PARAM_PROP_PREFIX + name.toLowerCase());
    }

    @Override
    public void appletResize(int width, int height) {
        Dimension frameSize = getSize();
        frameSize.width += width - appletSize.width;
        frameSize.height += height - appletSize.height;
        setSize(frameSize);
        appletSize = applet.getSize();
    }

    @Override
    public AppletContext getAppletContext() {
        return this;
    }

	@Override
	@SuppressWarnings("restriction")
    public AudioClip getAudioClip(URL url) {
        return new sun.applet.AppletAudioClip(url);
    }

    @Override
    public Image getImage(URL url) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        try {
            ImageProducer prod = (ImageProducer) url.getContent();
            return tk.createImage(prod);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Applet getApplet(String name) {
        if (name.equals(this.name)) {
            return applet;
        }
        return null;
    }

    @Override
    public Enumeration<Applet> getApplets() {
        Vector<Applet> v = new Vector<>();
        v.addElement(applet);
        return v.elements();
    }

    @Override
    public void showDocument(URL url) {
        // ignore
    }

    @Override
    public void showDocument(URL url, String target) {
        // ignore
    }

    @Override
    public void showStatus(String status) {
        if (label != null) {
            label.setText(status);
        }
    }

    @Override
    public void setStream(String key, InputStream stream) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public InputStream getStream(String key) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public Iterator<String> getStreamKeys() {
        throw new RuntimeException("Not supported");
    }
}
