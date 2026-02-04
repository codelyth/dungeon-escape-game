import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ZindanKacisiOyunu extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Aktif sahne tipini tutar
    private SahneTipi aktifSahneTipi = SahneTipi.ZINDAN;

    // Kaynaklar
    private int can = 100;
    private int kibrit = 5;

    // Etiketler
    private JLabel lblKaynaklar1, lblKaynaklar2, lblKaynaklar3, lblKaynaklar4;

    // Renk Paleti
    private final Color VURGU_RENGI = new Color(255, 215, 0); 

    // --- ENUM: Sahne Tipleri ---
    enum SahneTipi { ZINDAN, ORMAN }

    public ZindanKacisiOyunu() {
        setTitle("Zindan Kaçışı: Final Projesi");
        setSize(900, 750); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new ZindanPanel(cardLayout);
        
        // --- SAHNELER ---
        mainPanel.add(createAnaMenu(), "AnaMenu");
        mainPanel.add(createHikayeSahnesi(), "HikayeSahnesi");
        mainPanel.add(createKaynakBilgiSahnesi(), "KaynakBilgiSahnesi");
        mainPanel.add(createKararSahnesi1(), "Karar1");
        mainPanel.add(createKararSahnesi2(), "Karar2");
        mainPanel.add(createKararSahnesi3(), "Karar3");
        mainPanel.add(createKararSahnesi4(), "Karar4");
        mainPanel.add(new JPanel(), "OyunSonu");

        add(mainPanel);
        cardLayout.show(mainPanel, "AnaMenu");
    }

    // --- RESİM YÜKLEYEN ARKAPLAN SINIFI ---
    class ZindanPanel extends JPanel {
        private BufferedImage imgZindan;
        private BufferedImage imgOrman;

        public ZindanPanel(LayoutManager layout) { super(layout); resimleriYukle(); }
        public ZindanPanel() { super(); resimleriYukle(); }

        private void resimleriYukle() {
            try {
                imgZindan = ImageIO.read(new File("zindan_bg.jpg"));
                imgOrman = ImageIO.read(new File("orman_bg.jpg"));
            } catch (IOException e) {
                System.out.println("Hata: Resimler yüklenemedi. Dosya isimlerini kontrol et.");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage currentImg = (aktifSahneTipi == SahneTipi.ZINDAN) ? imgZindan : imgOrman;
            
            if (currentImg != null) {
                g.drawImage(currentImg, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(20, 20, 20)); 
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    // --- YARDIMCILAR ---
    
    // 1. KAYNAK ETİKETİ 
    private JLabel createResourceLabel() {
        JLabel lbl = new JLabel("", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        lbl.setFont(new Font("Serif", Font.BOLD, 22));
        lbl.setForeground(VURGU_RENGI);
        lbl.setOpaque(false); 
        lbl.setBackground(new Color(0, 0, 0, 180)); 
        lbl.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        lbl.setPreferredSize(new Dimension(500, 50));
        return lbl;
    }

    // 2. HİKAYE KUTUSU 
    private JPanel createTextPanel(String text) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setOpaque(true); 
        textPane.setBackground(new Color(0, 0, 0, 180)); 
        textPane.setForeground(new Color(230, 230, 230));
        textPane.setFont(new Font("Serif", Font.BOLD, 18)); 
        textPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        JScrollPane scroll = new JScrollPane(textPane);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150), 2));
        scroll.setPreferredSize(new Dimension(750, 450)); 

        wrapper.add(scroll); 
        return wrapper;
    }

    // 3. KARAR KUTUSU 
    private JPanel createDecisionPanel(String text) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setOpaque(true); 
        textPane.setBackground(new Color(0, 0, 0, 180)); 
        textPane.setForeground(new Color(230, 230, 230));
        textPane.setFont(new Font("Serif", Font.BOLD, 20)); 
        textPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        textPane.setPreferredSize(new Dimension(600, 200)); 
        wrapper.add(textPane); 
        return wrapper;
    }

    // 4. OYUN SONU KUTUSU
    private JPanel createEndGamePanel(String text) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JLabel label = new JLabel("<html><center>" + text.replace("\n", "<br>") + "</center></html>", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 200)); 
        label.setForeground(text.contains("TEBRİKLER") ? new Color(100, 255, 100) : new Color(255, 100, 100)); 
        label.setFont(new Font("Serif", Font.BOLD, 26)); 
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 3),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        wrapper.add(label);
        return wrapper;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(80, 20, 20));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Serif", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 20, 20), 2),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- ÖZEL UYARI PENCERESİ ---
    private void ozelUyariGoster(String baslik, String mesaj) {
        JDialog dialog = new JDialog(this, baslik, true); 
        dialog.setSize(550, 400);
        dialog.setLocationRelativeTo(this); 
        dialog.setUndecorated(true); 
        
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBackground(new Color(30, 30, 30));
        dialogPanel.setBorder(BorderFactory.createLineBorder(Color.RED.darker(), 4)); 

        JLabel titleLabel = new JLabel(baslik, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.RED); 
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        dialogPanel.add(titleLabel, BorderLayout.NORTH);
        // ---------------------------------------------

        JTextArea lblMesaj = new JTextArea("\n" + mesaj);
        lblMesaj.setFont(new Font("Serif", Font.BOLD, 18));
        lblMesaj.setForeground(Color.WHITE);
        lblMesaj.setOpaque(false);
        lblMesaj.setLineWrap(true);
        lblMesaj.setWrapStyleWord(true);
        lblMesaj.setEditable(false);
        lblMesaj.setMargin(new Insets(10, 30, 30, 30)); 

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JButton btnTamam = new JButton("DEVAM ET");
        styleButton(btnTamam); 
        btnTamam.setBackground(new Color(180, 20, 20));
        btnTamam.addActionListener(e -> dialog.dispose()); 

        btnPanel.add(btnTamam);
        dialogPanel.add(lblMesaj, BorderLayout.CENTER);
        dialogPanel.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(dialogPanel);
        dialog.setVisible(true);
    }

    // --- SAHNELER ---

    // 1. ANA MENÜ
    private JPanel createAnaMenu() {
        JPanel panel = new JPanel(new GridBagLayout()); 
        panel.setOpaque(false);
        JPanel contentBox = new JPanel(new GridLayout(3, 1, 20, 20));
        contentBox.setOpaque(false);

        JLabel lblBaslik = new JLabel("<html><center><font face='Serif' size='7' color='#ff0000'>KARANLIĞIN BEDELİ</font><br><font face='Serif' size='5' color='white'>Zindan Kaçışı</font></center></html>", SwingConstants.CENTER);

        JButton btnBasla = new JButton("HİKAYEYE BAŞLA");
        styleButton(btnBasla);
        JButton btnCikis = new JButton("ÇIKIŞ");
        styleButton(btnCikis);

        btnBasla.addActionListener(e -> {
            resetGame();
            cardLayout.show(mainPanel, "HikayeSahnesi");
        });
        btnCikis.addActionListener(e -> System.exit(0));

        contentBox.add(lblBaslik);
        contentBox.add(btnBasla);
        contentBox.add(btnCikis);
        panel.add(contentBox);
        return panel;
    }

    // 2. HİKAYE
    private JPanel createHikayeSahnesi() {
        JPanel panel = new JPanel(new BorderLayout()); 
        panel.setOpaque(false);

        String hikayeMetni = "\n...KARAKTER: ARİN (Eski Muhafız)...\n\n" +
                "Tam 3,650 gün... 'Kara Kule' zindanının en dibinde, güneşin unuttuğu bu rutubetli delikte çürüyorsun. " +
                "Bir zamanlar krallığın en onurlu kılıcıydın, şimdi ise sadece bir numarasın. " +
                "Tek suçun, yozlaşmış bir kralın emrine itaat etmemekti.\n\n" +
                "BUGÜN HER ŞEY DEĞİŞTİ! Yukarıda, kalede büyük bir isyan patlak verdi. " +
                "Top sesleri yeri göğü inletirken, sarsıntıyla hücrenin nemden çürümüş duvarı büyük bir gürültüyle çöktü.\n\n" +
                "Toz dumanın arasından sızan o serin hava... Bu, on yıldır hasret kaldığın özgürlüğün kokusu. " +
                "Ama aranızda ölümcül dehlizler ve sadık nöbetçiler var. Ya burada öleceksin ya da karanlığı yarıp geçeceksin. " +
                "Seçim senin Muhafız...";
        
        panel.add(createTextPanel(hikayeMetni), BorderLayout.CENTER);

        JButton btnDevam = new JButton("DEHLİZLERE GİR");
        styleButton(btnDevam); 
        btnDevam.setBackground(new Color(50, 50, 50));
        btnDevam.addActionListener(e -> cardLayout.show(mainPanel, "KaynakBilgiSahnesi"));
        
        JPanel bottomPanel = new JPanel(); 
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0,0,30,0)); 
        bottomPanel.add(btnDevam);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    // 3. KAYNAK BİLGİSİ
    private JPanel createKaynakBilgiSahnesi() {
        JPanel panel = new JPanel(new BorderLayout()); 
        panel.setOpaque(false);
        
        String bilgiMetni = "\n [ HAYATTA KALMA DURUMU ]\n\n" +
                "Yapacağın seçimler kaderini belirleyecek. O yüzden seçim yaparken iyi düşün! " +
                "Yolun uzun, kaynaklarını dikkatli kullan!\n\n" +
                "❤ CAN: 100\n   (0 olursa ölürsün)\n\n" +
                "🔥 KİBRİT: 5 Adet\n   (Karanlığı aydınlatır ama düşmana yerini belli eder)\n\n" +
                "Hazırsan, kaçış başlıyor Muhafız...";

        panel.add(createTextPanel(bilgiMetni), BorderLayout.CENTER);
        
        JButton btnBasla = new JButton("MACERAYA ATIL");
        styleButton(btnBasla); 
        btnBasla.setBackground(new Color(20, 80, 20)); 
        btnBasla.addActionListener(e -> {
            guncelleLabelMetni(); 
            cardLayout.show(mainPanel, "Karar1");
        });
        JPanel bottomPanel = new JPanel(); 
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0,0,30,0)); 
        bottomPanel.add(btnBasla);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    // 4. KARAR 1
    private JPanel createKararSahnesi1() {
        JPanel panel = new JPanel(new BorderLayout()); 
        panel.setOpaque(false);
        
        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        topWrapper.setOpaque(false);
        lblKaynaklar1 = createResourceLabel(); 
        topWrapper.add(lblKaynaklar1);
        panel.add(topWrapper, BorderLayout.NORTH);

        String kararMetni = "\nKoridorun sonuna geldin.\nİki yol var:\n\n" +
                "SOL: Zifiri karanlık.\n" +
                "SAĞ: Nöbetçi uyukluyor.";
        
        panel.add(createDecisionPanel(kararMetni), BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20)); 
        btnPanel.setOpaque(false);
        JButton btnSol = new JButton("SOL (Kibrit Yak)"); 
        JButton btnSag = new JButton("SAĞ (Sessiz Geç)");
        styleButton(btnSol); 
        styleButton(btnSag);
        
        btnSol.addActionListener(e -> {
            kibrit -= 1; can -= 40; 
            if (can > 0) { 
                guncelleKaynaklar(); 
                ozelUyariGoster("KİBRİT İHANET ETTİ!", "Kibritin ışığı seni hedef tahtası yaptı! Bir ok omzunu sıyırdı (-40 Can).\nAma karanlığa kaçıp kurtuldun."); 
                cardLayout.show(mainPanel, "Karar2"); 
            } else guncelleKaynaklar();
        });

        btnSag.addActionListener(e -> { 
            can -= 10; 
            guncelleKaynaklar(); 
            if (can > 0) {
                ozelUyariGoster("UCUZ ATLATILDI!", 
                        "Sessiz olmak için yerlerde sürünmek zorundaydın.\n" + 
                        "Keskin taşlar dizlerini parçaladı (-10 Can).\n" + 
                        "Ama en azından nöbetçi uyanmadı.");
                cardLayout.show(mainPanel, "Karar2"); 
            }
        });

        btnPanel.add(btnSol); 
        btnPanel.add(btnSag); 
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // 5. KARAR 2
    private JPanel createKararSahnesi2() {
        JPanel panel = new JPanel(new BorderLayout()); 
        panel.setOpaque(false);
        
        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        topWrapper.setOpaque(false);
        lblKaynaklar2 = createResourceLabel(); 
        topWrapper.add(lblKaynaklar2);
        panel.add(topWrapper, BorderLayout.NORTH);

        String kararMetni = "\nTavan çöktü! Yol kapandı.\n\n" +
                            "Kayaları itebilirsin veya\n" +
                            "kibritle delik arayabilirsin.";
        panel.add(createDecisionPanel(kararMetni), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20)); 
        btnPanel.setOpaque(false);
        JButton btnGuc = new JButton("TAŞLARI İT (Güç)"); 
        JButton btnAra = new JButton("TÜNEL ARA (Kibrit)");
        styleButton(btnGuc); 
        styleButton(btnAra);
        
        btnGuc.addActionListener(e -> { can -= 20; if (can > 0) { guncelleKaynaklar(); ozelUyariGoster("AĞIR YÜK!", "Kayaları kaldırırken ellerin parçalandı ve belini incittin (-20 Can).\nAma yolu açmayı başardın."); cardLayout.show(mainPanel, "Karar3"); } else guncelleKaynaklar(); });
        btnAra.addActionListener(e -> { kibrit -= 1; if (kibrit >= 0) { guncelleKaynaklar(); cardLayout.show(mainPanel, "Karar3"); } else { kibrit = 0; can -= 15; guncelleKaynaklar(); ozelUyariGoster("KİBRİTİN YOK!", "Karanlıkta yaralandın (-15 Can)."); if(can > 0) cardLayout.show(mainPanel, "Karar3"); } });
        btnPanel.add(btnGuc); btnPanel.add(btnAra); panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // 6. KARAR 3
    private JPanel createKararSahnesi3() {
        JPanel panel = new JPanel(new BorderLayout()); 
        panel.setOpaque(false);
        
        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        topWrapper.setOpaque(false);
        lblKaynaklar3 = createResourceLabel(); 
        topWrapper.add(lblKaynaklar3);
        panel.add(topWrapper, BorderLayout.NORTH);

        String kararMetni = "\nTerk edilmiş bir muhafız odası buldun.\n" +
                            "Burada biraz soluklanabilirsin.\n\n" +
                            "Masada bayat bir ekmek ve kilitli bir çekmece var.";
        panel.add(createDecisionPanel(kararMetni), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20)); 
        btnPanel.setOpaque(false);
        JButton btnYe = new JButton("EKMEĞİ YE (+Can)"); 
        JButton btnAra = new JButton("ÇEKMECEYİ ARA (+Eşya?)");
        styleButton(btnYe); 
        styleButton(btnAra);
        
        btnYe.addActionListener(e -> { can += 20; if (can > 100) can = 100; guncelleKaynaklar(); ozelUyariGoster("LEZZETLİ!", "Ekmek güç verdi (+20 Can)."); cardLayout.show(mainPanel, "Karar4"); });
        btnAra.addActionListener(e -> { if (Math.random() > 0.5) { kibrit += 2; guncelleKaynaklar(); ozelUyariGoster("ŞANSLI GÜNÜNDESİN!", "2 kibrit buldun!"); } else { can -= 5; guncelleKaynaklar(); ozelUyariGoster("ŞANSSIZLIĞIN BU KADARI!", "Çekmeceye bir fare saklanmıştı ve elini ısırdı! (-5 Can)"); } if (can > 0) cardLayout.show(mainPanel, "Karar4"); });
        btnPanel.add(btnYe); btnPanel.add(btnAra); panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // 7. KARAR 4
    private JPanel createKararSahnesi4() {
        JPanel panel = new JPanel(new BorderLayout()); 
        panel.setOpaque(false);
        
        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        topWrapper.setOpaque(false);
        lblKaynaklar4 = createResourceLabel(); 
        topWrapper.add(lblKaynaklar4);
        panel.add(topWrapper, BorderLayout.NORTH);

        String kararMetni = "\nSONUNDA! Çıkış kapısı karşında.\n" +
                            "Kapı kilitli ve arkasından orman kokusu geliyor.";
        panel.add(createDecisionPanel(kararMetni), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20)); 
        btnPanel.setOpaque(false);
        JButton btnZorla = new JButton("MAYMUNCUKLA (Sessiz)"); 
        JButton btnKir = new JButton("KAPIYI KIR (Gürültülü)");
        styleButton(btnZorla); 
        styleButton(btnKir);
        
        btnZorla.addActionListener(e -> checkWin(true));
        btnKir.addActionListener(e -> checkWin(false));
        btnPanel.add(btnZorla); btnPanel.add(btnKir); panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // 8. OYUN SONU
    private JPanel createOyunSonu(String mesaj) {
        JPanel panel = new JPanel(new BorderLayout()); 
        panel.setOpaque(false);
        panel.add(createEndGamePanel(mesaj), BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(); 
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0,0,50,0));
        JButton btnTekrar = new JButton("BAŞA DÖN"); 
        styleButton(btnTekrar);
        btnTekrar.addActionListener(e -> { resetGame(); cardLayout.show(mainPanel, "AnaMenu"); });
        btnPanel.add(btnTekrar); panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // --- MANTIK ---
    private void guncelleLabelMetni() {
        String metin = "[ CAN: " + can + " ]   |   [ KİBRİT: " + kibrit + " ]";
        if (lblKaynaklar1 != null) lblKaynaklar1.setText(metin);
        if (lblKaynaklar2 != null) lblKaynaklar2.setText(metin);
        if (lblKaynaklar3 != null) lblKaynaklar3.setText(metin);
        if (lblKaynaklar4 != null) lblKaynaklar4.setText(metin);
    }
    
    private void guncelleKaynaklar() {
        guncelleLabelMetni();
        if (can <= 0) { 
            aktifSahneTipi = SahneTipi.ZINDAN;
            mainPanel.repaint(); 
            showEndScreen("ÖLDÜN!\nZindanın karanlığına gömüldün."); 
        } else if (kibrit < 0) { kibrit = 0; guncelleLabelMetni(); }
    }
    
    private void checkWin(boolean isSessiz) {
        if (isSessiz) {
            aktifSahneTipi = SahneTipi.ORMAN; 
            mainPanel.repaint(); 
            showEndScreen("TEBRİKLER!\nKapıyı sessizce açtın ve\normanda kayboldun.");
        }
        else showEndScreen("YAKALANDIN!\nGürültüye bütün nöbetçiler geldi.");
    }
    
    private void showEndScreen(String mesaj) {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible() == false && comp instanceof JPanel) {
                // Temizlik
            }
        }
        JPanel sonSahne = createOyunSonu(mesaj);
        mainPanel.add(sonSahne, "OyunSonu");
        cardLayout.show(mainPanel, "OyunSonu");
    }
    
    private void resetGame() {
        can = 100; kibrit = 5;
        aktifSahneTipi = SahneTipi.ZINDAN; 
        mainPanel.repaint();
        guncelleLabelMetni(); 
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { new ZindanKacisiOyunu().setVisible(true); });
    }
}