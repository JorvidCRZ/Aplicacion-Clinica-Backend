package com.proyectoClinica.service.export;

import com.proyectoClinica.model.Paciente;
import com.proyectoClinica.model.Persona;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReporteExcelExporter {

    public byte[] exportar(List<Paciente> pacientes) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Reporte de Pacientes");

            int[] widths = {1500, 4500, 4500, 2500, 3000, 3000, 2000, 3000, 3500, 4500, 6000, 2500};
            for (int i = 0; i < widths.length; i++) {
                sheet.setColumnWidth(i, widths[i]);
            }

            // Ocultar líneas de cuadrícula
            sheet.setDisplayGridlines(false);

            // Reducir márgenes
            sheet.setMargin(Sheet.LeftMargin, 0.5);
            sheet.setMargin(Sheet.RightMargin, 0.5);
            sheet.setMargin(Sheet.TopMargin, 0.5);
            sheet.setMargin(Sheet.BottomMargin, 0.5);

            // Estilos
            CellStyle styleTitulo = workbook.createCellStyle();
            Font fontTitulo = workbook.createFont();
            fontTitulo.setBold(true);
            fontTitulo.setFontHeightInPoints((short) 16);
            styleTitulo.setFont(fontTitulo);
            styleTitulo.setAlignment(HorizontalAlignment.CENTER);

            CellStyle styleSubtitulo = workbook.createCellStyle();
            Font fontSubtitulo = workbook.createFont();
            fontSubtitulo.setBold(true);
            fontSubtitulo.setFontHeightInPoints((short) 12);
            styleSubtitulo.setFont(fontSubtitulo);
            styleSubtitulo.setAlignment(HorizontalAlignment.CENTER);

            CellStyle styleHeader = workbook.createCellStyle();
            Font fontHeader = workbook.createFont();
            fontHeader.setBold(true);
            fontHeader.setColor(IndexedColors.WHITE.getIndex());
            styleHeader.setFont(fontHeader);
            styleHeader.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleHeader.setAlignment(HorizontalAlignment.CENTER);
            styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
            styleHeader.setBorderTop(BorderStyle.THIN);
            styleHeader.setBorderBottom(BorderStyle.THIN);
            styleHeader.setBorderLeft(BorderStyle.THIN);
            styleHeader.setBorderRight(BorderStyle.THIN);

            CellStyle styleBody = workbook.createCellStyle();
            styleBody.setAlignment(HorizontalAlignment.CENTER);
            styleBody.setVerticalAlignment(VerticalAlignment.CENTER);
            styleBody.setBorderTop(BorderStyle.THIN);
            styleBody.setBorderBottom(BorderStyle.THIN);
            styleBody.setBorderLeft(BorderStyle.THIN);
            styleBody.setBorderRight(BorderStyle.THIN);
            styleBody.setWrapText(true);

            CellStyle styleFechaHora = workbook.createCellStyle();
            Font fontFechaHora = workbook.createFont();
            fontFechaHora.setFontHeightInPoints((short) 10);
            styleFechaHora.setFont(fontFechaHora);
            styleFechaHora.setAlignment(HorizontalAlignment.RIGHT);

            // Encabezado principal
            int rowNum = 0;

            // === Inserción del LOGO ===
            try (InputStream is = new FileInputStream("src/main/resources/static/images/logo.png")) {
                byte[] bytes = IOUtils.toByteArray(is);
                int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                CreationHelper helper = workbook.getCreationHelper();
                Drawing<?> drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(1);
                anchor.setRow1(1);
                anchor.setCol2(2);
                anchor.setRow2(3);
                Picture pict = drawing.createPicture(anchor, pictureIdx);
                pict.resize(1);
            } catch (Exception e) {
                System.err.println("No se pudo cargar el logo: " + e.getMessage());
            }

            Row rowTitulo = sheet.createRow(rowNum++);
            Cell cellTitulo = rowTitulo.createCell(0);
            cellTitulo.setCellValue("CLÍNICA TU SALUD");
            cellTitulo.setCellStyle(styleTitulo);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));

            Row rowSubtitulo = sheet.createRow(rowNum++);
            Cell cellSubtitulo = rowSubtitulo.createCell(0);
            cellSubtitulo.setCellValue("Reporte de Pacientes");
            cellSubtitulo.setCellStyle(styleSubtitulo);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 11));

            // Fecha y hora
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

            Row rowFecha = sheet.createRow(rowNum++);
            Cell cellFecha = rowFecha.createCell(11);
            cellFecha.setCellValue("Fecha: " + ahora.format(formatoFecha));
            cellFecha.setCellStyle(styleFechaHora);

            Row rowHora = sheet.createRow(rowNum++);
            Cell cellHora = rowHora.createCell(11);
            cellHora.setCellValue("Hora: " + ahora.format(formatoHora));
            cellHora.setCellStyle(styleFechaHora);

            rowNum++; // Espacio visual

            // Encabezados de tabla
            String[] headers = {
                    "ID", "Nombre", "Apellido", "Tipo Doc", "DNI", "Fecha Nac.",
                    "Edad", "Género", "Teléfono", "Correo", "Dirección", "Estado"
            };

            Row headerRow = sheet.createRow(rowNum++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(styleHeader);
            }

            // Cuerpo del reporte
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for (Paciente p : pacientes) {
                Persona persona = p.getPersona();
                Row row = sheet.createRow(rowNum++);

                LocalDate fechaNac = persona.getFechaNacimiento();
                int edad = (fechaNac != null) ? Period.between(fechaNac, LocalDate.now()).getYears() : 0;

                int col = 0;
                row.createCell(col++).setCellValue(p.getIdPaciente());
                row.createCell(col++).setCellValue(persona.getNombre1() + " " + (persona.getNombre2() != null ? persona.getNombre2() : ""));
                row.createCell(col++).setCellValue(persona.getApellidoPaterno() + " " + persona.getApellidoMaterno());
                row.createCell(col++).setCellValue(persona.getTipoDocumento());
                row.createCell(col++).setCellValue(persona.getDni());
                row.createCell(col++).setCellValue(fechaNac != null ? fechaNac.format(formato) : "");
                row.createCell(col++).setCellValue(edad);
                row.createCell(col++).setCellValue(persona.getGenero());
                row.createCell(col++).setCellValue(persona.getTelefono());
                row.createCell(col++).setCellValue(p.getUsuarioAgrego() != null ? p.getUsuarioAgrego().getCorreo() : "");
                row.createCell(col++).setCellValue(persona.getDireccion());
                row.createCell(col++).setCellValue("Activo");

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(styleBody);
                }
            }

            // Área visible
            int ultimaFila = rowNum - 1;
            workbook.setPrintArea(
                    workbook.getSheetIndex(sheet),
                    0,
                    headers.length - 1,
                    0,
                    ultimaFila
            );

            sheet.getPrintSetup().setFitWidth((short) 1);
            sheet.getPrintSetup().setFitHeight((short) 0);

            workbook.write(baos);
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando el Excel de pacientes", e);
        }
    }
}
