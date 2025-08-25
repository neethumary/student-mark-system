package com.example.sms;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

public class MainController {

    @FXML private TableView<Student> tableView;
    @FXML private TableColumn<Student, String> nameCol;
    @FXML private TableColumn<Student, Integer> c1;
    @FXML private TableColumn<Student, Integer> c2;
    @FXML private TableColumn<Student, Integer> c3;
    @FXML private TableColumn<Student, Integer> c4;
    @FXML private TableColumn<Student, Integer> c5;
    @FXML private TableColumn<Student, Integer> c6;
    @FXML private TableColumn<Student, Integer> c7;
    @FXML private TableColumn<Student, Integer> c8;
    @FXML private TableColumn<Student, Integer> c9;
    @FXML private TableColumn<Student, Integer> c10;

    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private Button exportPdfBtn;
    @FXML private Label selectionHelp;

    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private final List<String> subjects = Arrays.asList(
            "English","Maths","Science","History","Geography",
            "Computer","Physics","Chemistry","Biology","Arts"
    );

    @FXML
    public void initialize() {
        // Demo data (>= 20 students to test pagination)
        Random r = new Random(42);
        for (int i = 1; i <= 25; i++) {
            students.add(new Student(
                    "Student " + i,
                    40 + r.nextInt(61), 40 + r.nextInt(61), 40 + r.nextInt(61),
                    40 + r.nextInt(61), 40 + r.nextInt(61), 40 + r.nextInt(61),
                    40 + r.nextInt(61), 40 + r.nextInt(61), 40 + r.nextInt(61),
                    40 + r.nextInt(61)
            ));
        }

        // Table setup
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        c1.setCellValueFactory(new PropertyValueFactory<>("s1"));
        c2.setCellValueFactory(new PropertyValueFactory<>("s2"));
        c3.setCellValueFactory(new PropertyValueFactory<>("s3"));
        c4.setCellValueFactory(new PropertyValueFactory<>("s4"));
        c5.setCellValueFactory(new PropertyValueFactory<>("s5"));
        c6.setCellValueFactory(new PropertyValueFactory<>("s6"));
        c7.setCellValueFactory(new PropertyValueFactory<>("s7"));
        c8.setCellValueFactory(new PropertyValueFactory<>("s8"));
        c9.setCellValueFactory(new PropertyValueFactory<>("s9"));
        c10.setCellValueFactory(new PropertyValueFactory<>("s10"));
        tableView.setItems(students);

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> updateChart());
        tableView.getSelectionModel().getSelectedItems()
        .addListener((javafx.collections.ListChangeListener<Student>) change -> updateChart());


        // Chart setup
        xAxis.setCategories(FXCollections.observableArrayList(subjects));
        yAxis.setLabel("Marks");
        updateChart();

        selectionHelp.setText("Tip: Use Ctrl/Shift to select multiple students and compare.");
    }

    private void updateChart() {
        barChart.getData().clear();
        ObservableList<Student> selected = tableView.getSelectionModel().getSelectedItems();
        if (selected == null || selected.isEmpty()) {
            // If nothing selected, show first 1 student to avoid empty chart
            if (!students.isEmpty()) {
                selected = FXCollections.observableArrayList(students.get(0));
            }
        }
        for (Student s : selected) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(s.getName());
            series.getData().add(new XYChart.Data<>(subjects.get(0), s.getS1()));
            series.getData().add(new XYChart.Data<>(subjects.get(1), s.getS2()));
            series.getData().add(new XYChart.Data<>(subjects.get(2), s.getS3()));
            series.getData().add(new XYChart.Data<>(subjects.get(3), s.getS4()));
            series.getData().add(new XYChart.Data<>(subjects.get(4), s.getS5()));
            series.getData().add(new XYChart.Data<>(subjects.get(5), s.getS6()));
            series.getData().add(new XYChart.Data<>(subjects.get(6), s.getS7()));
            series.getData().add(new XYChart.Data<>(subjects.get(7), s.getS8()));
            series.getData().add(new XYChart.Data<>(subjects.get(8), s.getS9()));
            series.getData().add(new XYChart.Data<>(subjects.get(9), s.getS10()));
            barChart.getData().add(series);
        }
    }

    @FXML
    private void exportToPDF() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save PDF");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fc.setInitialFileName("student-report.pdf");
        File file = fc.showSaveDialog(barChart.getScene().getWindow());
        if (file == null) return;

        try {
            createPdf(file);
            new Alert(Alert.AlertType.INFORMATION, "PDF exported to: " + file.getAbsolutePath(), ButtonType.OK).showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to export PDF: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private void createPdf(File file) throws IOException, DocumentException {
        Document doc = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter.getInstance(doc, new FileOutputStream(file));
        doc.open();

        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Student Mark Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
        doc.add(new Paragraph(" "));

        // 1) TABLE (Name + 10 subject columns)
        PdfPTable table = new PdfPTable(11); // Name + 10 subjects
        table.setWidthPercentage(100);
        table.setHeaderRows(1);

        Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        addHeaderCell(table, "Name", headerFont);
        for (String sub : subjects) addHeaderCell(table, sub, headerFont);

        int rowCount = 0;
        for (Student s : students) {
            addCell(table, s.getName());
            addCell(table, String.valueOf(s.getS1()));
            addCell(table, String.valueOf(s.getS2()));
            addCell(table, String.valueOf(s.getS3()));
            addCell(table, String.valueOf(s.getS4()));
            addCell(table, String.valueOf(s.getS5()));
            addCell(table, String.valueOf(s.getS6()));
            addCell(table, String.valueOf(s.getS7()));
            addCell(table, String.valueOf(s.getS8()));
            addCell(table, String.valueOf(s.getS9()));
            addCell(table, String.valueOf(s.getS10()));
            rowCount++;
            // After every 10 students, force a new section/page
            if (rowCount % 10 == 0) {
                doc.add(table);
                doc.newPage();
                // Recreate table with headers for the next section
                table = new PdfPTable(11);
                table.setWidthPercentage(100);
                table.setHeaderRows(1);
                addHeaderCell(table, "Name", headerFont);
                for (String sub : subjects) addHeaderCell(table, sub, headerFont);
            }
        }
        if (rowCount % 10 != 0) {
            doc.add(table);
        }

        // 2) Add a snapshot of the chart (current selection)
        doc.newPage();
        Paragraph chartTitle = new Paragraph("Selected Students - Marks per Subject", headerFont);
        chartTitle.setAlignment(Element.ALIGN_CENTER);
        doc.add(chartTitle);
        doc.add(new Paragraph(" "));

        WritableImage fxImage = barChart.snapshot(new SnapshotParameters(), null);
        BufferedImage bimg = SwingFXUtils.fromFXImage(fxImage, null);
        File tempPng = File.createTempFile("chart", ".png");
        ImageIO.write(bimg, "png", tempPng);

        Image chartImage = Image.getInstance(tempPng.getAbsolutePath());
        float maxWidth = PageSize.A4.getHeight() - 60; // because landscape used earlier; here we use default portrait page after newPage(), but we can scale safely
        if (chartImage.getScaledWidth() > maxWidth) {
            chartImage.scaleToFit(maxWidth, maxWidth);
        }
        chartImage.setAlignment(Element.ALIGN_CENTER);
        doc.add(chartImage);

        doc.close();
        tempPng.delete();
    }

    private void addHeaderCell(PdfPTable table, String text, Font headerFont) {
        PdfPCell cell = new PdfPCell(new Phrase(text, headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
