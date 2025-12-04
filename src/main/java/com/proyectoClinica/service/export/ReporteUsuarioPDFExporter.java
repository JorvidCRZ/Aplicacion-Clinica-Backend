package com.proyectoClinica.service.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.proyectoClinica.model.Usuario; // Importa tu modelo Usuario
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReporteUsuarioPDFExporter {

    public byte[] exportar(List<Usuario> data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Documento en formato A4 (portrait) para mejor vista de la tabla
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font fontSubtitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontContenido = new Font(Font.FontFamily.HELVETICA, 9);

            LocalDateTime ahora = LocalDateTime.now();
            String fechaActual = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String horaActual = ahora.format(DateTimeFormatter.ofPattern("HH:mm"));

            // ======= ENCABEZADO =======
            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.setWidths(new float[]{1f, 4f});

            Image logo = null;
            try (InputStream is = getClass().getResourceAsStream("/static/images/logo.png")) {
                if (is != null) {
                    logo = Image.getInstance(is.readAllBytes());
                    logo.scaleToFit(90, 90);
                }
            }

            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            if (logo != null) logoCell.addElement(logo);
            encabezado.addCell(logoCell);

            PdfPCell textCell = new PdfPCell();
            textCell.setBorder(Rectangle.NO_BORDER);

            Paragraph titulo = new Paragraph("CLÍNICA TU SALUD", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            Paragraph subtitulo = new Paragraph("Reporte de Usuarios", fontSubtitulo);
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

            // ======= TABLA DE USUARIOS =======
            document.add(new Paragraph("Listado de Usuarios Registrados", fontSubtitulo));
            document.add(Chunk.NEWLINE);

            PdfPTable tabla = new PdfPTable(7);
            tabla.setWidthPercentage(100);
            // Definición de anchos de columna (ajustar según necesidad)
            tabla.setWidths(new float[]{1f, 4f, 4f, 2f, 2f, 2f, 2.5f});

            BaseColor headerColor = new BaseColor(0, 102, 204);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 9);

            // Headers
            agregarHeader(tabla, "ID", headerFont, headerColor);
            agregarHeader(tabla, "Nombre Completo", headerFont, headerColor);
            agregarHeader(tabla, "Email", headerFont, headerColor);
            agregarHeader(tabla, "Rol", headerFont, headerColor);
            agregarHeader(tabla, "Teléfono", headerFont, headerColor);
            agregarHeader(tabla, "Tipo Doc.", headerFont, headerColor);
            agregarHeader(tabla, "Número Doc.", headerFont, headerColor);


            // Cuerpo de la Tabla
            for (Usuario usuario : data) {
                // Navegación a través de las relaciones
                String nombreCompleto = usuario.getPersona().getNombre1() + " "
                        + (usuario.getPersona().getNombre2() != null ? usuario.getPersona().getNombre2() + " " : "")
                        + usuario.getPersona().getApellidoPaterno() + " "
                        + usuario.getPersona().getApellidoMaterno();

                agregarCelda(tabla, String.valueOf(usuario.getIdUsuario()), bodyFont);
                agregarCelda(tabla, nombreCompleto, bodyFont);
                agregarCelda(tabla, usuario.getCorreo(), bodyFont);
                agregarCelda(tabla, usuario.getRol() != null ? usuario.getRol().getNombre() : "N/A", bodyFont);
                agregarCelda(tabla, usuario.getPersona().getTelefono(), bodyFont);
                agregarCelda(tabla, usuario.getPersona().getTipoDocumento(), bodyFont);
                agregarCelda(tabla, usuario.getPersona().getDni(), bodyFont);
            }

            if (data.isEmpty()) {
                PdfPCell noData = new PdfPCell(new Phrase("No hay usuarios registrados", bodyFont));
                noData.setColspan(7);
                noData.setHorizontalAlignment(Element.ALIGN_CENTER);
                noData.setPadding(8);
                tabla.addCell(noData);
            }

            document.add(tabla);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF Reporte Usuarios: " + e.getMessage(), e);
        }
    }

    private void agregarHeader(PdfPTable tabla, String titulo, Font font, BaseColor bg) {
        PdfPCell cell = new PdfPCell(new Phrase(titulo, font));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        tabla.addCell(cell);
    }

    private void agregarCelda(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto != null ? texto : "—", font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}