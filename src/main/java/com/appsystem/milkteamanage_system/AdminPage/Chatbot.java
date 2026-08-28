package com.appsystem.milkteamanage_system.AdminPage;

import com.appsystem.milkteamanage_system.Utils.DBConnection;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import org.json.JSONArray;
import org.json.JSONObject;


public class Chatbot extends JPanel {

    private JTextArea chatArea;
    private JTextField inputField;  
    private JButton sendButton;
    private final HttpClient httpClient;
    private final Map<String[], Object[]> knowledgeBase;

    private final String API_KEY = "sk-9158534eca754fcdb828a29f9d5a5617";
    private final String API_ENDPOINT = "https://api.deepseek.com/chat/completions";

    public Chatbot() {
        this.httpClient = HttpClient.newHttpClient();
        this.knowledgeBase = createKnowledgeBase();
        initComponents();
    }

    private Map<String[], Object[]> createKnowledgeBase() {
        Map<String[], Object[]> kb = new HashMap<>();

        kb.put(new String[]{"đổi mật khẩu", "thay đổi pass"}, 
               new Object[]{"Để đổi mật khẩu, người dùng cần vào mục 'Thông tin cá nhân'...", null});
        
        kb.put(new String[]{"thêm sản phẩm", "tạo món mới"}, 
               new Object[]{"Để thêm sản phẩm mới, Admin vào mục 'Quản Lí Hàng Hoá'...", null});

        // === THAY ĐỔI: Bổ sung thêm "doanh thu quán ngày" để linh hoạt hơn ===
        kb.put(new String[]{"doanh thu ngày", "doanh thu hôm", "doanh thu mùng", "doanh thu của ngày", "doanh thu quán ngày"},
               new Object[]{
                   "SELECT ISNULL(SUM(TotalAmount), 0) FROM Orders WHERE CAST(OrderDate AS DATE) = ?", 
                   "DATE"
               });
        
        // --- Các truy vấn không cần entity nhưng vẫn là SQL ---
        kb.put(new String[]{"doanh thu tháng này"},
               new Object[]{"SELECT ISNULL(SUM(TotalAmount), 0) FROM Orders WHERE MONTH(OrderDate) = MONTH(GETDATE()) AND YEAR(OrderDate) = YEAR(GETDATE())", null});

        kb.put(new String[]{"bán chạy nhất", "đắt hàng nhất"},
               new Object[]{"SELECT TOP 1 p.Name FROM OrderDetails od JOIN Products p ON od.ProductID = p.ProductID JOIN Orders o ON od.OrderID = o.OrderID GROUP BY p.Name ORDER BY SUM(od.Quantity) DESC", null});
        
        // --- Trường hợp mặc định cho "doanh thu" (nếu không có ngày cụ thể) ---
        kb.put(new String[]{"doanh thu hôm nay"},
               new Object[]{"SELECT ISNULL(SUM(TotalAmount), 0) FROM Orders WHERE CAST(OrderDate AS DATE) = CAST(GETDATE() AS DATE)", null});

        return kb;
    }

    private void initComponents() {
        // ... (Giữ nguyên code giao diện từ trước, không thay đổi)
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 247, 250));
        JLabel titleLabel = new JLabel("Trợ lý AI", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setMargin(new java.awt.Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(new Color(245, 247, 250));
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.addActionListener(e -> sendMessage());
        sendButton = new JButton("Gửi");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 123, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(e -> sendMessage());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
        chatArea.setText("Bot: Xin chào! Tôi là trợ lý ảo của quán. Hãy thử hỏi tôi: 'Doanh thu quán ngày 16/7 là bao nhiêu?'\n");
    }

    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) return;
        chatArea.append("Bạn: " + userInput + "\n");
        inputField.setText("");
        setInteractionEnabled(false);

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String context = findRelevantContext(userInput);
                return getChatbotResponse(userInput, context);
            }
            @Override
            protected void done() {
                try {
                    String response = get();
                    chatArea.append("Bot: " + response + "\n");
                } catch (Exception e) {
                    chatArea.append("Bot: Xin lỗi, đã có lỗi xảy ra khi xử lý yêu cầu của bạn.\n");
                    e.printStackTrace();
                } finally {
                    setInteractionEnabled(true);
                }
            }
        }.execute();
    }

    /**
     * Trích xuất ngày từ chuỗi văn bản người dùng.
     * @param text Chuỗi đầu vào.
     * @return Một đối tượng LocalDate nếu tìm thấy, ngược lại trả về null.
     */
    private LocalDate parseDateFromInput(String text) {
        text = text.toLowerCase();
        if (text.contains("hôm nay")) return LocalDate.now();
        if (text.contains("hôm qua")) return LocalDate.now().minusDays(1);

        // Tìm kiếm các mẫu ngày/tháng như "16/7", "16-7"
        Pattern pattern = Pattern.compile("(\\d{1,2})[/-](\\d{1,2})");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            try {
                int day = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(2));
                int year = LocalDate.now().getYear(); // Mặc định là năm hiện tại
                // Giả định nếu tháng trong tương lai so với tháng hiện tại, có thể người dùng muốn hỏi năm ngoái
                // (Logic này có thể được cải tiến thêm)
                return LocalDate.of(year, month, day);
            } catch (Exception e) {
                return null; // Ngày không hợp lệ (ví dụ: 32/13)
            }
        }
        return null;
    }

    private String findRelevantContext(String userInput) {
        String lowerUserInput = userInput.toLowerCase();
        String bestMatchKeyword = null;
        Object[] instruction = null;

        // Tìm từ khóa dài nhất, cụ thể nhất khớp với câu hỏi
        for (Map.Entry<String[], Object[]> entry : knowledgeBase.entrySet()) {
            for (String keyword : entry.getKey()) {
                if (lowerUserInput.contains(keyword)) {
                    if (bestMatchKeyword == null || keyword.length() > bestMatchKeyword.length()) {
                        bestMatchKeyword = keyword;
                        instruction = entry.getValue();
                    }
                }
            }
        }

        if (instruction != null) {
            String template = (String) instruction[0];
            String entityType = (String) instruction[1];

            // Nếu đây là một câu lệnh SQL
            if (template.toUpperCase().startsWith("SELECT")) {
                try (Connection conn = DBConnection.getConnection()) {
                    PreparedStatement pst;
                    // Nếu cần tìm entity là DATE
                    if ("DATE".equals(entityType)) {
                        LocalDate date = parseDateFromInput(lowerUserInput);
                        if (date == null) {
                            return "Tôi chưa nhận diện được ngày tháng trong câu hỏi của bạn. Vui lòng thử lại với định dạng dd/mm.";
                        }
                        pst = conn.prepareStatement(template);
                        pst.setDate(1, java.sql.Date.valueOf(date));
                    } else { // SQL đơn giản, không cần tham số
                        pst = conn.prepareStatement(template);
                    }

                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            try {
                                double amount = rs.getDouble(1);
                                String formattedResult = com.appsystem.milkteamanage_system.Utils.Utils.formatCurrency(amount);
                                return "Dữ liệu từ cửa hàng cho thấy: " + formattedResult;
                            } catch (Exception e_format) {
                                return "Dữ liệu từ cửa hàng cho thấy: " + rs.getString(1);
                            }
                        } else {
                            return "Không tìm thấy dữ liệu phù hợp trong cửa hàng.";
                        }
                    }
                } catch (Exception e_sql) {
                    e_sql.printStackTrace();
                    return "Lỗi khi truy vấn dữ liệu cửa hàng.";
                }
            } else { // Nếu là hướng dẫn thông thường
                return template;
            }
        }
        return null; // Không tìm thấy context
    }
    
    // Phương thức getChatbotResponse và các phương thức khác giữ nguyên
    private String getChatbotResponse(String userInput, String context) throws IOException, InterruptedException {
        if (API_KEY.equals("YOUR_DEEPSEEK_OR_OPENAI_API_KEY")) {
            return "Vui lòng cấu hình API Key trong file Chatbot.java";
        }
        JSONArray messages = new JSONArray();
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "Bạn là một trợ lý AI am hiểu về phần mềm quản lý quán trà sữa. Hãy trả lời câu hỏi của người dùng một cách thân thiện, chuyên nghiệp, ngắn gọn và đi thẳng vào vấn đề dựa trên thông tin được cung cấp. Nếu không có thông tin, hãy trả lời một cách thông thường.");
        messages.put(systemMessage);
        if (context != null && !context.isEmpty()) {
            JSONObject contextMessage = new JSONObject();
            contextMessage.put("role", "system");
            contextMessage.put("content", "Hãy dựa vào thông tin sau đây để trả lời: \"" + context + "\"");
            messages.put(contextMessage);
        }
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", userInput);
        messages.put(userMessage);
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", messages);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JSONObject responseJson = new JSONObject(response.body());
            return responseJson.getJSONArray("choices")
                               .getJSONObject(0)
                               .getJSONObject("message")
                               .getString("content");
        } else {
            System.err.println("API Error: " + response.body());
            return "Lỗi kết nối tới AI service. Status code: " + response.statusCode();
        }
    }
    
    private void setInteractionEnabled(boolean enabled) {
        inputField.setEnabled(enabled);
        sendButton.setEnabled(enabled);
        if(enabled) {
            inputField.requestFocusInWindow();
        }
    }
}