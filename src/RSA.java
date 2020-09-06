/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author AngleOfPeace
 */
public class RSA extends javax.swing.JFrame {

    public int ks;
    private File file, pub, pri;

    /**
     * Creates new form RSA
     */
    public RSA() {
        initComponents();
    }

    public void genrateKey(String key) throws NoSuchAlgorithmException, IOException {

        SecureRandom sr = new SecureRandom(key.getBytes());
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(ks, sr);

        KeyPair kp = kpg.genKeyPair();

        PublicKey publicKey = kp.getPublic();

        PrivateKey privateKey = kp.getPrivate();

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        File publicKeyFile = null;
        File privateKeyFile = null;

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            publicKeyFile = createKeyFile(new File(chooser.getSelectedFile() + "\\" + "publicKey.rsa"));
            privateKeyFile = createKeyFile(new File(chooser.getSelectedFile() + "\\" + "privateKey.rsa"));
        } else {
            JOptionPane.showMessageDialog(null, "Folder not selected");
        }

        FileOutputStream fos = new FileOutputStream(publicKeyFile);
        fos.write(publicKey.getEncoded());
        fos.close();

        fos = new FileOutputStream(privateKeyFile);
        fos.write(privateKey.getEncoded());
        fos.close();

    }

    private static File createKeyFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }
        return file;
    }

    public void setKeysize(int KS) {
        ks = KS;
    }

    public byte[] encrypt(byte[] b, byte[] p) throws Exception {

        X509EncodedKeySpec spec = new X509EncodedKeySpec(p);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = factory.generatePublic(spec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] encrypted = cipher.doFinal(b);
        return encrypted;
    }

    public byte[] decrypt(byte[] toDecrypt, byte[] p) throws Exception {

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(p);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = factory.generatePrivate(spec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] decrypted = cipher.doFinal(toDecrypt);
        return decrypted;
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

        jScrollPane1 = new javax.swing.JScrollPane();
        Txtkey = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TxtPubkey = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        BtnGekey = new javax.swing.JButton();
        BtnEnc = new javax.swing.JButton();
        BtnDec = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        TxtKeysize = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        TxtPrikey = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        TxtFilesize = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MILoadfile = new javax.swing.JMenuItem();
        MILoadfolder = new javax.swing.JMenuItem();
        MILoadkey = new javax.swing.JMenuItem();
        MILoadpub = new javax.swing.JMenuItem();
        MILoadpri = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("RSA");

        Txtkey.setColumns(20);
        Txtkey.setRows(5);
        jScrollPane1.setViewportView(Txtkey);

        jLabel1.setText("Key");

        TxtPubkey.setColumns(20);
        TxtPubkey.setRows(5);
        jScrollPane2.setViewportView(TxtPubkey);

        jLabel2.setText("Key public for encrypt");

        BtnGekey.setText("Generated key");
        BtnGekey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGekeyActionPerformed(evt);
            }
        });

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

        jLabel3.setText("Key sizes");

        TxtKeysize.setText("1024");

        jLabel4.setText("Key private for decrypt");

        TxtPrikey.setColumns(20);
        TxtPrikey.setRows(5);
        jScrollPane3.setViewportView(TxtPrikey);

        jLabel5.setText("File sizes");

        TxtFilesize.setEditable(false);

        jLabel6.setText("Bytes");

        jMenu1.setText("File");
        jMenu1.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N

        MILoadfile.setText("Load file");
        MILoadfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MILoadfileActionPerformed(evt);
            }
        });
        jMenu1.add(MILoadfile);

        MILoadfolder.setText("Load folder");
        MILoadfolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MILoadfolderActionPerformed(evt);
            }
        });
        jMenu1.add(MILoadfolder);

        MILoadkey.setText("Load key(.txt)");
        MILoadkey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MILoadkeyActionPerformed(evt);
            }
        });
        jMenu1.add(MILoadkey);

        MILoadpub.setText("Load public key");
        MILoadpub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MILoadpubActionPerformed(evt);
            }
        });
        jMenu1.add(MILoadpub);

        MILoadpri.setText("Load private key");
        MILoadpri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MILoadpriActionPerformed(evt);
            }
        });
        jMenu1.add(MILoadpri);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 171, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(TxtFilesize, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(BtnGekey)))
                                .addGap(16, 16, 16))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(TxtKeysize, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BtnEnc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BtnDec)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnEnc)
                    .addComponent(BtnDec)
                    .addComponent(jLabel3)
                    .addComponent(TxtKeysize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BtnGekey)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(8, 8, 8)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(8, 8, 8)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(TxtFilesize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnEncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEncActionPerformed
        try {
            byte[] p = Files.readAllBytes(pub.toPath());

            if (file.isFile()) {
                byte[] b = Files.readAllBytes(file.toPath());
                byte[] encrypt = encrypt(b, p);

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
                    byte[] encrypt = encrypt(b, p);

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
            byte[] p = Files.readAllBytes(pri.toPath());

            if (file.isFile()) {
                byte[] b = Files.readAllBytes(file.toPath());
                byte[] decrypt = decrypt(b, p);

                File file1 = new File(file.getPath().replaceAll(Pattern.quote("(Enc)"), ""));
                FileOutputStream fout = new FileOutputStream(file1);
                fout.write(decrypt);
                fout.close();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                new File(file.getPath().replaceAll(Pattern.quote("(Enc)"), "")).mkdirs();
                File file3 = new File(file.getPath().replaceAll(Pattern.quote("(Enc)"), ""));
                for (File file1 : files) {
                    byte[] b = Files.readAllBytes(file1.toPath());
                    byte[] decrypt = decrypt(b, p);

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

    private void BtnGekeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGekeyActionPerformed
        try {
            setKeysize(Integer.parseInt(TxtKeysize.getText()));
            TxtFilesize.setText(String.valueOf(Integer.parseInt(TxtKeysize.getText()) / 8 - 11));
            genrateKey(Txtkey.getText());
        } catch (NoSuchAlgorithmException | IOException ex) {
            JOptionPane.showMessageDialog(null, "Key not generated " + ex);
        }
    }//GEN-LAST:event_BtnGekeyActionPerformed

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

    private void MILoadpubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MILoadpubActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("RSA FILES", "rsa", "rsa");
        fileChooser.setFileFilter(filter);
        File file3 = null;
        int selection = fileChooser.showOpenDialog(this);
        if (selection == JFileChooser.APPROVE_OPTION) {
            file3 = fileChooser.getSelectedFile();
            pub = file3;
        }

        if (file3 != null) {
            try {
                BufferedReader in;
                in = new BufferedReader(new FileReader(file3));
                String line = in.readLine();
                while (line != null) {
                    TxtPubkey.append(Base64.getEncoder().encodeToString(line.getBytes()));
                    line = in.readLine();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Key public not loaded " + ex);
            }
        }
    }//GEN-LAST:event_MILoadpubActionPerformed

    private void MILoadpriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MILoadpriActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("RSA FILES", "rsa", "rsa");
        fileChooser.setFileFilter(filter);
        File file3 = null;
        int selection = fileChooser.showOpenDialog(this);
        if (selection == JFileChooser.APPROVE_OPTION) {
            file3 = fileChooser.getSelectedFile();
            pri = file3;
        }

        if (file3 != null) {
            try {
                BufferedReader in;
                in = new BufferedReader(new FileReader(file3));
                String line = in.readLine();
                while (line != null) {
                    TxtPrikey.append(Base64.getEncoder().encodeToString(line.getBytes()));
                    line = in.readLine();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Key private not loaded " + ex);
            }
        }
    }//GEN-LAST:event_MILoadpriActionPerformed

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
                    Txtkey.append(line);
                    line = in.readLine();
                    TxtKeysize.setText(line);
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
            java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RSA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RSA().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnDec;
    private javax.swing.JButton BtnEnc;
    private javax.swing.JButton BtnGekey;
    private javax.swing.JMenuItem MILoadfile;
    private javax.swing.JMenuItem MILoadfolder;
    private javax.swing.JMenuItem MILoadkey;
    private javax.swing.JMenuItem MILoadpri;
    private javax.swing.JMenuItem MILoadpub;
    private javax.swing.JTextField TxtFilesize;
    private javax.swing.JTextField TxtKeysize;
    private javax.swing.JTextArea TxtPrikey;
    private javax.swing.JTextArea TxtPubkey;
    private javax.swing.JTextArea Txtkey;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
