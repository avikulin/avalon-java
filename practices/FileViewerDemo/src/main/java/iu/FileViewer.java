package iu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FileViewer extends JFrame{
    private final JList fileList;
    private final JTextArea fileContent;
    private File[] files;
    public FileViewer() {
        setBounds(900,100, 600, 400);
        fileList = new JList();
        fileList.addListSelectionListener(e -> fileListItemSelection());
        fileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if ((e.getButton() == MouseEvent.BUTTON1)&&(e.getClickCount() == 2)){
                    enterToDir();
                }
            }
        });

        fileList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    enterToDir();
                }
            }
        });

        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        fileContent = new JTextArea();
        fileContent.setEditable(false);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(fileList), new JScrollPane(fileContent));

        sp.setDividerLocation(150);

        add(sp, BorderLayout.CENTER);
        gotToDir(new File(System.getProperty("user.dir")));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void enterToDir(){
        int selectedIdx = fileList.getSelectedIndex();
        if (selectedIdx == -1 )
            return;
        File f = files[selectedIdx];
        if (!f.isDirectory())
            return;

        gotToDir(f);
    }
    private void fileListItemSelection(){
        int selectedIdx = fileList.getSelectedIndex();
        if (selectedIdx == -1){
            return;
        }
        File file = files[selectedIdx];
        if (file.isDirectory()){
            fileContent.setText("");
        } else {
            StringBuilder content = new StringBuilder();
            char[] buffer = new char[4096];
            int res;
            try {
                    FileReader fr = new FileReader(file);
                    while ((res = fr.read(buffer)) >= 0){
                        content.append(buffer, 0, res);
                    }
            } catch (IOException e) {
                fileContent.setText(e.getLocalizedMessage());
            }

            fileContent.setText(content.toString());
            fileContent.setCaretPosition(0);
        }
    }

    private void gotToDir(File file) {
        File[] tempList = file.listFiles();
        if (tempList == null){
            fileContent.setText("Error reading file list of " + file.getAbsolutePath()+".");
            return;
        }
        files = tempList;
        this.setTitle(file.getAbsolutePath());

        Arrays.sort(files, (o1, o2) -> {
            if (o1.isDirectory()&&o2.isFile()){
                return -1;
            }

            if (o2.isDirectory()&&o1.isFile()){
                return 1;
            }

            return o1.getName().compareTo(o2.getName());
        });

        File parent = file.getParentFile();
        if (parent != null){
            File[] af = new File[files.length + 1];
            af[0] = parent;
            System.arraycopy(files, 0, af, 1, files.length);
            files = af;
        }

        String[] fileNames = new String[files.length];
        for(int i=0; i<files.length; i++){
            fileNames[i] = files[i].getName();
        }

        if (parent!=null){
            fileNames[0] = "..";
        }
        fileList.setListData(fileNames);
    }

    public static void main(String[] args) {
        new FileViewer().setVisible(true);
    }
}
