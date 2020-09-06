/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.util.Base64;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author AngleOfPeace
 */
public class RC6 extends javax.swing.JFrame {

    SecretKey sk;
    public int ks;
    private File file;

    /**
     * Creates new form RC6
     */
    public RC6() {
        initComponents();
        Security.addProvider(new BouncyCastleProvider());
    }

    public void genrateKey(String key) throws Exception {
        SecureRandom sr = new SecureRandom(key.getBytes());
        KeyGenerator kg = KeyGenerator.getInstance("RC6");
        kg.init(ks, sr);
        sk = kg.generateKey();
    }

    public byte[] encrypt(byte[] b) throws Exception {

        Cipher cipher = Cipher.getInstance("RC6");
        cipher.init(Cipher.ENCRYPT_MODE, sk);
        byte[] encrypted = cipher.doFinal(b);
        return encrypted;
    }

    public byte[] decrypt(byte[] toDecrypt) throws Exception {

        Cipher cipher = Cipher.getInstance("RC6");
        cipher.init(Cipher.DECRYPT_MODE, sk);
        byte[] decrypted = cipher.doFinal(toDecrypt);
        return decrypted;
    }

    public void setKeysize(int KS) {
        ks = KS;
    }

    public void zipFile(String filePath) {
        try {
            File file2 = new File(filePath);

            FileOutputStream fos = new FileOutputStream(file2.getAbsolutePath().concat(".zip"));
            ZipOutputStream zos = new ZipOutputStream(fos);

            zos.putNextEntry(new ZipEntry(file2.getName()));

            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            zos.write(bytes, 0, bytes.length);

            zos.closeEntry();
            zos.close();
            fos.close();

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "The file %s does not exist");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Can not zip file ");
        }
    }

    public void zipFolder(String filePath) throws Exception {

        File file2 = new File(filePath);
        Path sourceFolderPath = Paths.get(filePath);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file2.getAbsolutePath().concat(".zip")));
        Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                try {
                    zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                    Files.copy(file, zos);
                    zos.closeEntry();

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Can not zip folder ");
                }
                return FileVisitResult.CONTINUE;
            }
        });
        zos.close();
    }

    public void deleteDirectory(String directoryFilePath) throws IOException {
        Path directory = Paths.get(directoryFilePath);

        if (Files.exists(directory)) {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) {
                    try {
                        Files.delete(path);

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Can not delete file ");
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path directory, IOException ioException) {
                    try {
                        Files.delete(directory);

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Can not delete folder ");
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TxtKey = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        TxtKeysize = new javax.swing.JTextField();
        BtnEnc = new javax.swing.JButton();
        BtnDec = new javax.swing.JButton();
        BtnGeK = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TxtKG = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        MIfile = new javax.swing.JMenu();
        MILoadfile = new javax.swing.JMenuItem();
        MILoadfolder = new javax.swing.JMenuItem();
        MILoadkey = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("RC6");

        jLabel1.setText("Key");

        TxtKey.setColumns(20);
        TxtKey.setRows(5);
        jScrollPane1.setViewportView(TxtKey);

        jLabel2.setText("Keysize(1->128)");

        TxtKeysize.setText("128");

        BtnEnc.setText("Encrypt");
        BtnEnc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEncActionPerformed(evt);
            }
        });

        BtnDec.setText("Decrypt");
        BtnDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDecActionPerformed(evt);
            }
        });

        BtnGeK.setText("Generate key");
        BtnGeK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGeKActionPerformed(evt);
            }
        });

        jLabel3.setText("Key Generated");

        TxtKG.setColumns(20);
        TxtKG.setRows(5);
        jScrollPane2.setViewportView(TxtKG);

        MIfile.setText("File");
        MIfile.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        MILoadfile.setText("Load file");
        MILoadfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MILoadfileActionPerformed(evt);
            }
        });
        MIfile.add(MILoadfile);

        MILoadfolder.setText("Load folder");
        MILoadfolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MILoadfolderActionPerformed(evt);
            }
        });
        MIfile.add(MILoadfolder);

        MILoadkey.setText("Load key(.txt)");
        MILoadkey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MILoadkeyActionPerformed(evt);
            }
        });
        MIfile.add(MILoadkey);

        jMenuBar1.add(MIfile);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TxtKeysize, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnEnc)
                        .addGap(18, 18, 18)
                        .addComponent(BtnDec)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnGeK)
                .addGap(46, 46, 46))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TxtKeysize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnEnc)
                    .addComponent(BtnDec))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnGeK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnEncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEncActionPerformed
        try {

            if (file.isFile()) {
                byte[] encrypt = encrypt(Files.readAllBytes(file.toPath()));

                File file1 = new File(file.getPath().replaceAll(Pattern.quote("."), "(Enc)."));
                FileOutputStream fout = new FileOutputStream(file1);
                fout.write(encrypt);
                fout.close();

                zipFile(file1.getAbsolutePath());
                file1.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                new File(file.getPath().concat("(Enc)")).mkdirs();
                File file3 = new File(file.getPath().concat("(Enc)"));
                for (File file1 : files) {

                    byte[] b = Files.readAllBytes(file1.toPath());
                    byte[] encrypt = encrypt(b);
                    File file2 = new File(file1.getPath().replaceAll(Pattern.quote("."), "(Enc)."));
                    FileOutputStream fout = new FileOutputStream(file2);
                    fout.write(encrypt);
                    fout.close();

                    Files.move(file2.toPath(), Paths.get(file3.getPath() + "\\" + file2.getName()));
                }
                zipFolder(file3.getAbsolutePath());
                deleteDirectory(file3.getPath());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Can not encrypted " + ex);
        }
    }//GEN-LAST:event_BtnEncActionPerformed

    private void BtnDecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDecActionPerformed
        try {
            if (file.isFile()) {
                byte[] b = Files.readAllBytes(file.toPath());
                byte[] decrypt = decrypt(b);

                String decfile = file.getPath().replaceAll(Pattern.quote("(Enc)"), "");
                File file1 = new File(decfile);
                FileOutputStream fout = new FileOutputStream(file1);
                fout.write(decrypt);
                fout.close();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                new File(file.getPath().replaceAll(Pattern.quote("(Enc)"), "")).mkdirs();
                File file3 = new File(file.getPath().replaceAll(Pattern.quote("(Enc)"), ""));
                for (File file1 : files) {

                    byte[] b = Files.readAllBytes(file1.toPath());
                    byte[] decrypt = decrypt(b);
                    File file2 = new File(file1.getPath().replaceAll(Pattern.quote("(Enc)"), ""));
                    FileOutputStream fout = new FileOutputStream(file2);
                    fout.write(decrypt);
                    fout.close();

                    Files.move(file2.toPath(), Paths.get(file3.getPath() + "\\" + file2.getName()));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Can not decrypted " + ex);
        }
    }//GEN-LAST:event_BtnDecActionPerformed

    private void BtnGeKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGeKActionPerformed
        try {
            setKeysize(Integer.parseInt(TxtKeysize.getText()));
            genrateKey(TxtKey.getText());
            TxtKG.setText(Base64.getEncoder().encodeToString(sk.getEncoded()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Key not generated " + ex);
        }
    }//GEN-LAST:event_BtnGeKActionPerformed

    private void MILoadfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MILoadfileActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        int selection = fileChooser.showOpenDialog(this);
        try {
            if (selection == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "File not selected");
        }
    }//GEN-LAST:event_MILoadfileActionPerformed

    private void MILoadfolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MILoadfolderActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
        } else {
            JOptionPane.showMessageDialog(null, "Folder not selected");
        }
    }//GEN-LAST:event_MILoadfolderActionPerformed

    private void MILoadkeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MILoadkeyActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fileChooser.setFileFilter(filter);
        File file3 = null;

        int selection = fileChooser.showOpenDialog(this);
        if (selection == JFileChooser.APPROVE_OPTION) {
            file3 = fileChooser.getSelectedFile();
        }

        if (file3 != null) {
            try {
                BufferedReader in;
                in = new BufferedReader(new FileReader(file3));
                String line = in.readLine();
                while (line != null) {
                    TxtKey.append(line);
                    line = in.readLine();
                }
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Key not selected " + ex);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Key not readed " + ex);
            }
        }
    }//GEN-LAST:event_MILoadkeyActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RC6.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RC6.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RC6.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RC6.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RC6().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnDec;
    private javax.swing.JButton BtnEnc;
    private javax.swing.JButton BtnGeK;
    private javax.swing.JMenuItem MILoadfile;
    private javax.swing.JMenuItem MILoadfolder;
    private javax.swing.JMenuItem MILoadkey;
    private javax.swing.JMenu MIfile;
    private javax.swing.JTextArea TxtKG;
    private javax.swing.JTextArea TxtKey;
    private javax.swing.JTextField TxtKeysize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
