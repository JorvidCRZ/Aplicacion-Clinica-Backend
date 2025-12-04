package com.proyectoClinica.service.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.proyectoClinica.dto.response.ReporteRendimientoDoctorDTO;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReporteRendimientoDoctorPDFExporter {

    public byte[] exportar(List<ReporteRendimientoDoctorDTO> data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate(), 36, 36, 50, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            // FUENTES
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontContenido = new Font(Font.FontFamily.HELVETICA, 9);

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 102, 204);

            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 9);

            LocalDateTime now = LocalDateTime.now();
            String fechaActual = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaActual = now.format(DateTimeFormatter.ofPattern("HH:mm"));

            // ENCABEZADO
            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.setWidths(new float[]{1f, 4f});

            Image logo = null;
            try (InputStream is = getClass().getResourceAsStream("/static/images/logo.png")) {
                if (is != null) {
                    logo = Image.getInstance(is.readAllBytes());
                    logo.scaleToFit(100, 100);
                }
            }

            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            if (logo != null) logoCell.addElement(logo);
            encabezado.addCell(logoCell);

            PdfPCell textCell = new PdfPCell();
            textCell.setBorder(Rectangle.NO_BORDER);
            textCell.setPaddingLeft(20);

            Paragraph titulo = new Paragraph("CLÍNICA TU SALUD", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);

            Paragraph subtitulo = new Paragraph("Reporte de Rendimiento de Doctores", fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);

            Paragraph fechaP = new Paragraph("Fecha: " + fechaActual, fontContenido);
            fechaP.setAlignment(Element.ALIGN_RIGHT);

            Paragraph horaP = new Paragraph("Hora: " + horaActual, fontContenido);
            horaP.setAlignment(Element.ALIGN_RIGHT);

            textCell.addElement(titulo);
            textCell.addElement(subtitulo);
            textCell.addElement(fechaP);
            textCell.addElement(horaP);

            encabezado.addCell(textCell);
            document.add(encabezado);
            document.add(Chunk.NEWLINE);

            // ============= TABLA ===============
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4f, 3f, 2f, 2f, 2f, 2f, 3f});

            addHeader(table, "Doctor", headerFont, headerColor);
            addHeader(table, "Especialidad", headerFont, headerColor);
            addHeader(table, "Totales", headerFont, headerColor);
            addHeader(table, "Completadas", headerFont, headerColor);
            addHeader(table, "Canceladas", headerFont, headerColor);
            addHeader(table, "Pendientes", headerFont, headerColor);
            addHeader(table, "Ingresos", headerFont, headerColor);

            if (data.isEmpty()) {
                PdfPCell noData = new PdfPCell(new Phrase("No hay datos disponibles.", bodyFont));
                noData.setColspan(7);
                noData.setHorizontalAlignment(Element.ALIGN_CENTER);
                noData.setPadding(8);
                table.addCell(noData);
            } else {
                for (ReporteRendimientoDoctorDTO d : data) {
                    addCell(table, d.getNombreDoctor(), bodyFont);
                    addCell(table, d.getEspecialidad(), bodyFont);
                    addCell(table, String.valueOf(d.getTotalCitas()), bodyFont);
                    addCell(table, String.valueOf(d.getCitasCompletadas()), bodyFont);
                    addCell(table, String.valueOf(d.getCitasCanceladas()), bodyFont);
                    addCell(table, String.valueOf(d.getCitasPendientes()), bodyFont);
                    addCell(table, "S/ " + d.getIngresosTotales(), bodyFont);
                }
            }

            document.add(table);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF rendimiento doctor: " + e.getMessage(), e);
        }
    }

    private void addHeader(PdfPTable tabla, String titulo, Font font, BaseColor bg) {
        PdfPCell cell = new PdfPCell(new Phrase(titulo, font));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        tabla.addCell(cell);
    }

    private void addCell(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "—", font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}
