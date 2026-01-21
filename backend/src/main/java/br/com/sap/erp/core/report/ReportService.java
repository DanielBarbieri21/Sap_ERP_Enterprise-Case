package br.com.sap.erp.core.report;

// import net.sf.jasperreports.engine.*;
// import net.sf.jasperreports.engine.data.JREmptyDataSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    public byte[] generateFinancialStatement(LocalDate startDate, LocalDate endDate) {
        LocalDate start = startDate != null ? startDate : LocalDate.now().withDayOfMonth(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        try (InputStream template = getClass().getResourceAsStream("/reports/financial_statement.jrxml");
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            if (template == null) {
                throw new IllegalStateException("Template financial_statement.jrxml não encontrado");
            }

            // JasperReport jasperReport = JasperCompileManager.compileReport(template);
            Map<String, Object> params = new HashMap<>();
            params.put("START_DATE", start);
            params.put("END_DATE", end);

            // JasperPrint print = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
            // JasperExportManager.exportReportToPdfStream(print, out);
            // Temporary stub - JasperReports disabled due to CVE-2025-10492
            return new byte[0];
            // return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar relatório financeiro", e);
        }
    }
}
