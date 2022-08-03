package imageview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import script.Features;

/**
 * The implementation of image gui view.
 */
public class ImageGuiViewImp extends JFrame implements ImageGuiView {
  private static final long serialVersionUID = 1L;

  private JLabel imageCanvas;
  private JTextArea textArea;
  private JButton loadButton;
  private JButton saveButton;
  private JButton compareButton;
  private JButton undoButton;
  private JButton redoButton;
  private JButton resetButton;
  private JButton batchProcessButton;
  private JButton userManualButton;
  private JButton aboutMeButton;
  private JButton exitButton;

  private JMenuItem blurItem;
  private JMenuItem sharpenItem;
  private JMenuItem grayscaleItem;
  private JMenuItem sepiaItem;
  private JMenuItem ditherItem;
  private JMenuItem mosaicItem;
  private JMenuItem edgeDetectionItem;
  private JMenuItem grayscaleContractEnhancementItem;

  private JMenuItem loadItem;
  private JMenuItem saveItem;
  private JMenuItem batchProcessItem;
  private JMenuItem exitItem;
  private JMenuItem undoItem;
  private JMenuItem redoItem;
  private JMenuItem resetItem;
  private JMenuItem userManualItem;
  private JMenuItem aboutMeItem;

  /**
   * Constructor.
   *
   * @param caption    the initial text to display
   */
  public ImageGuiViewImp(String caption) {
    super(caption);

    this.setPreferredSize(new Dimension(1200, 900));
    this.setMinimumSize(new Dimension(1200, 900));
    Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
    setLocation(center.x - 600, center.y - 450);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    this.setBackground(new Color(43, 43, 43));
    this.setFocusable(true);
    this.setFocusTraversalKeysEnabled(false);

    renderMenu();
    renderImageCanvas();
    renderLogCanvas();
    renderButtons();

    pack();
    setVisible(true);
  }

  @Override
  public void addFeatures(Features features) {
    ActionListener loadListener = e -> {
      URI uri = null;
      try {
        uri = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
      } catch (URISyntaxException ex) {
        throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
      }
      JFileChooser fileChooser = uri == null ? new JFileChooser() : new JFileChooser(new File(uri));
      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        features.loadImage(fileChooser.getSelectedFile().getAbsolutePath());
      }
      updateImage(features.getCurrentImage());
    };
    loadItem.addActionListener(loadListener);
    loadButton.addActionListener(loadListener);
    this.getRootPane().registerKeyboardAction(
            loadListener,
            KeyStroke.getKeyStroke(KeyEvent.VK_O,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
            JComponent.WHEN_IN_FOCUSED_WINDOW);

    ActionListener saveListener = e -> {
      URI uri = null;
      try {
        uri = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
      } catch (URISyntaxException ex) {
        throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
      }
      JFileChooser fileChooser = uri == null ? new JFileChooser() : new JFileChooser(new File(uri));
      if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
        features.saveImage(fileChooser.getSelectedFile().getAbsolutePath());
      }
    };
    saveItem.addActionListener(saveListener);
    saveButton.addActionListener(saveListener);
    this.getRootPane().registerKeyboardAction(
            saveListener,
            KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
            JComponent.WHEN_IN_FOCUSED_WINDOW);

    ActionListener batchProcessListener = e -> {
      URI uri = null;
      try {
        uri = getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
      } catch (URISyntaxException ex) {
        throw new IllegalStateException("Could not create URI object: " + ex.getMessage(), ex);
      }
      JFileChooser fileChooser = uri == null ? new JFileChooser() : new JFileChooser(new File(uri));
      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        features.batchProcess(fileChooser.getSelectedFile().getAbsolutePath());
      }
    };
    batchProcessItem.addActionListener(batchProcessListener);
    batchProcessButton.addActionListener(batchProcessListener);
    this.getRootPane().registerKeyboardAction(
            batchProcessListener,
            KeyStroke.getKeyStroke(KeyEvent.VK_B,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
            JComponent.WHEN_IN_FOCUSED_WINDOW);

    exitItem.addActionListener(e -> System.exit(0));
    exitButton.addActionListener(e -> System.exit(0));

    ActionListener undoListener = e -> {
      if (features.undo()) {
        updateImage(features.getCurrentImage());
        redoButton.setEnabled(true);
      }
    };
    undoItem.addActionListener(undoListener);
    undoButton.addActionListener(undoListener);
    this.getRootPane().registerKeyboardAction(
            undoListener,
            KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
            JComponent.WHEN_IN_FOCUSED_WINDOW);

    ActionListener redoListener = e -> {
      features.redo();
      updateImage(features.getCurrentImage());
      redoButton.setEnabled(false);
    };
    redoItem.addActionListener(redoListener);
    redoButton.addActionListener(redoListener);
    this.getRootPane().registerKeyboardAction(
            redoListener,
            KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                    KeyEvent.SHIFT_MASK
                    + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
            JComponent.WHEN_IN_FOCUSED_WINDOW);

    ActionListener resetListener = e -> {
      if (features.reset()) {
        updateImage(features.getCurrentImage());
        redoButton.setEnabled(true);
      }
    };
    resetItem.addActionListener(resetListener);
    resetButton.addActionListener(resetListener);
    this.getRootPane().registerKeyboardAction(
            resetListener,
            KeyStroke.getKeyStroke(KeyEvent.VK_R,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
            JComponent.WHEN_IN_FOCUSED_WINDOW);

    ActionListener userManualListener = e -> {
      JFrame manualFrame = new JFrame();
      manualFrame.setPreferredSize(new Dimension(800, 600));
      manualFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      manualFrame.setLayout(new BorderLayout());

      JTextArea t = new JTextArea(50, 10);
      t.setEditable(false);
      JScrollPane logScrollPane = new JScrollPane(t);
      logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      logScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      t.append(features.getUserManual());

      manualFrame.add(logScrollPane);
      manualFrame.pack();
      manualFrame.setVisible(true);
    };
    userManualItem.addActionListener(userManualListener);
    userManualButton.addActionListener(userManualListener);

    ActionListener aboutMeListener = e -> {
      JFrame manualFrame = new JFrame();
      manualFrame.setPreferredSize(new Dimension(400, 300));
      manualFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      manualFrame.setLayout(new BorderLayout());

      JTextArea t = new JTextArea(50, 10);
      t.setEditable(false);
      JScrollPane logScrollPane = new JScrollPane(t);
      logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      logScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      t.append(features.getAboutMe());

      manualFrame.add(logScrollPane);
      manualFrame.pack();
      manualFrame.setVisible(true);
    };
    aboutMeItem.addActionListener(aboutMeListener);
    aboutMeButton.addActionListener(aboutMeListener);

    compareButton.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) { }

      @Override
      public void mousePressed(MouseEvent e) {
        updateImage(features.getOriginImage());
        compareButton.setText("Showing Origin");
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        updateImage(features.getCurrentImage());
        compareButton.setText("Compare");
      }

      @Override
      public void mouseEntered(MouseEvent e) { }

      @Override
      public void mouseExited(MouseEvent e) { }
    });

    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {

      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
          updateImage(features.getOriginImage());
          compareButton.setText("Showing Origin");
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
          updateImage(features.getCurrentImage());
          compareButton.setText("Compare");
        }
      }
    });

    blurItem.addActionListener(e -> {
      features.blur();
      updateImage(features.getCurrentImage());
    });

    sharpenItem.addActionListener(e -> {
      features.sharpen();
      updateImage(features.getCurrentImage());
    });

    grayscaleItem.addActionListener(e -> {
      features.grayscale();
      updateImage(features.getCurrentImage());
    });

    sepiaItem.addActionListener(e -> {
      features.sepia();
      updateImage(features.getCurrentImage());
    });

    ditherItem.addActionListener(e -> {
      features.dither();
      updateImage(features.getCurrentImage());
    });

    edgeDetectionItem.addActionListener(e -> {
      features.edgeDetection();
      updateImage(features.getCurrentImage());
    });

    grayscaleContractEnhancementItem.addActionListener(e -> {
      features.grayscaleContractEnhancement();
      updateImage(features.getCurrentImage());
    });

    mosaicItem.addActionListener(e -> {
      int seeds = 20;
      JLabel label = new JLabel("Mosaic Level: ");
      JTextField textField = new JTextField(String.valueOf(seeds));
      JPanel panel = new JPanel(new GridLayout(0, 1));
      panel.setPreferredSize(new Dimension(500, 300));
      JSlider slider = new JSlider(0, 1000, 20);
      slider.setPaintTicks(true);
      slider.setMinorTickSpacing(10);
      slider.setPaintTrack(true);
      slider.setMajorTickSpacing(100);
      slider.setPaintLabels(true);
      slider.setSnapToTicks(true);
      slider.addChangeListener(e1 -> textField.setText(String.valueOf(slider.getValue())));

      panel.add(label);
      panel.add(textField);
      panel.add(slider);

      int result = JOptionPane.showConfirmDialog(this, panel, "",
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

      if (result == JOptionPane.OK_OPTION) {
        int setSeeds = Integer.parseInt(textField.getText());
        seeds = setSeeds == 0 ? 1 : setSeeds;
      }

      features.mosaic(seeds);
      updateImage(features.getCurrentImage());
    });
  }

  @Override
  public void showMessage(String message) {
    // redirects data to the text area
    textArea.append(message + '\n');
    // scrolls the text area to the end of data
    textArea.setCaretPosition(textArea.getDocument().getLength());
  }

  private void renderMenu() {
    JMenuBar manuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    loadItem = new JMenuItem("Load");
    saveItem = new JMenuItem("Save");
    batchProcessItem = new JMenuItem("Batch Process");
    exitItem = new JMenuItem("Exit");

    fileMenu.add(loadItem);
    fileMenu.add(saveItem);
    fileMenu.add(batchProcessItem);
    fileMenu.add(exitItem);
    manuBar.add(fileMenu);

    JMenu editMenu = new JMenu("Edit");
    undoItem = new JMenuItem("Undo");
    redoItem = new JMenuItem("Redo");
    resetItem = new JMenuItem("Reset");
    editMenu.add(undoItem);
    editMenu.add(redoItem);
    editMenu.add(resetItem);
    manuBar.add(editMenu);

    JMenu filterMenu = new JMenu("Filter");
    blurItem = new JMenuItem("blur");
    sharpenItem = new JMenuItem("sharpen");
    grayscaleItem = new JMenuItem("grayscale");
    sepiaItem = new JMenuItem("sepia");
    ditherItem = new JMenuItem("dither");
    mosaicItem = new JMenuItem("mosaic");
    edgeDetectionItem = new JMenuItem(("edgeDetection"));
    grayscaleContractEnhancementItem = new JMenuItem("grayscaleContractEnhancement");

    filterMenu.add(blurItem);
    filterMenu.add(sharpenItem);
    filterMenu.add(grayscaleItem);
    filterMenu.add(sepiaItem);
    filterMenu.add(ditherItem);
    filterMenu.add(mosaicItem);
    filterMenu.add(edgeDetectionItem);
    filterMenu.add(grayscaleContractEnhancementItem);

    manuBar.add(filterMenu);

    JMenu helpMenu = new JMenu("Help");
    userManualItem = new JMenuItem("User Manual");
    aboutMeItem = new JMenuItem("About Me");
    helpMenu.add(userManualItem);
    helpMenu.add(aboutMeItem);
    manuBar.add(helpMenu);

    this.setJMenuBar(manuBar);
  }

  private void renderImageCanvas() {
    JPanel canvas = new JPanel();
    imageCanvas = new JLabel();
    canvas.add(imageCanvas);
    canvas.setBackground(new Color(60, 63, 65));
    JScrollPane imageScrollPane = new JScrollPane(canvas);
    imageScrollPane.setPreferredSize(new Dimension(800, 600));
    imageScrollPane.setMinimumSize(new Dimension(800, 600));
    imageScrollPane.setBounds(0, 0, 800, 600);
    imageScrollPane.setBackground(new Color(60, 63, 65));
    this.getContentPane().add(imageScrollPane);
  }

  private void updateImage(BufferedImage image) {
    if (image != null) {
      imageCanvas.setIcon(new ImageIcon(image));
      this.setVisible(true);
    }
  }

  private void renderLogCanvas() {
    textArea = new JTextArea(50, 10);
    textArea.setEditable(false);
    textArea.setForeground(new Color(185, 185, 185));
    textArea.setBackground(new Color(43, 43, 43));
    textArea.setPreferredSize(new Dimension(1200, 200));
    textArea.setMinimumSize(new Dimension(1200, 200));
    textArea.setMaximumSize(new Dimension(9999, 200));
    textArea.setCaretPosition(0);
    JScrollPane logScrollPane = new JScrollPane(textArea);
    logScrollPane.setPreferredSize(new Dimension(1200, 200));
    logScrollPane.setMinimumSize(new Dimension(1200, 200));
    logScrollPane.setMaximumSize(new Dimension(9999, 200));
    logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    logScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    this.add(logScrollPane, BorderLayout.SOUTH);
  }

  private void renderButtons() {
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new GridLayout(10, 1, 20, 3));
    buttonsPanel.setPreferredSize(new Dimension(200, 600));
    buttonsPanel.setMinimumSize(new Dimension(200, 600));
    buttonsPanel.setMaximumSize(new Dimension(200, 9999));
    buttonsPanel.setBackground(new Color(43, 43, 43));
    buttonsPanel.setOpaque(true);

    loadButton = new JButton("Load");
    buttonsPanel.add(loadButton);

    saveButton = new JButton("Save");
    buttonsPanel.add(saveButton);

    compareButton = new JButton("Compare");
    buttonsPanel.add(compareButton);

    undoButton = new JButton("Undo");
    buttonsPanel.add(undoButton);

    redoButton = new JButton("Redo");
    redoButton.setEnabled(false);
    buttonsPanel.add(redoButton);

    resetButton = new JButton("Reset");
    buttonsPanel.add(resetButton);

    batchProcessButton = new JButton("Batch Process");
    buttonsPanel.add(batchProcessButton);

    userManualButton = new JButton("User Manual");
    buttonsPanel.add(userManualButton);

    aboutMeButton = new JButton("About Me");
    buttonsPanel.add(aboutMeButton);

    exitButton = new JButton("Exit");
    buttonsPanel.add(exitButton);

    for (int i = 0; i < buttonsPanel.getComponentCount(); i++) {
      JButton b = (JButton) buttonsPanel.getComponent(i);
      b.setBackground(new Color(43, 43, 43));
      b.setForeground(new Color(238, 169, 45));
      b.setFont(new Font("", Font.BOLD, 20));
      b.setOpaque(true);
    }

    this.add(buttonsPanel, BorderLayout.EAST);
  }
}
