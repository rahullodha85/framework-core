package com.hbcd.reporting.controllers;

import com.hbcd.logging.log.Log;
import com.hbcd.reporting.impl.CaseRecordImpl;
import com.hbcd.reporting.impl.ModuleRecordImpl;
import com.hbcd.reporting.impl.StepRecordImpl;
import com.hbcd.reporting.objects.Case;
import com.hbcd.reporting.objects.Module;
import com.hbcd.reporting.objects.Step;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelController extends ControllerBase implements Controller {
    /**
     * intiailize excel
     */
    private static Workbook workbook;
    private static HSSFSheet modules;
    private static HSSFSheet cases;
    private static HSSFSheet steps;
    private FileOutputStream fileOut;

    private static int moduleRowCount = 1;
    private static int caseRowCount = 1;
    private static int stepRowCount = 1;
    private static boolean isInit = false;

    public void init() throws Exception {
        super.init();
        try {
            if (!isInit) {
                deleteExistingExcel();
                workbook = new HSSFWorkbook();
                modules = (HSSFSheet) workbook.createSheet("modules");
                cases = (HSSFSheet) workbook.createSheet("cases");
                steps = (HSSFSheet) workbook.createSheet("steps");
                setModuleColumnTitles();
                setCaseTitles();
                setStepTitles();
                isInit = true;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int storeModule(ModuleRecordImpl moduleInput) throws Exception {
        Module moduleRow = new Module(moduleInput.getModuleRow());
        setRowValues(moduleRowCount,
                modules,
                String.valueOf(moduleRowCount),
                moduleRow.getModuleType() + "",
                moduleRow.getModuleScheduler() + "",
                moduleRow.getModuleScheduleTime().toString(),
                moduleRow.getModuleStatus() + "");
        moduleRowCount++;
        return moduleRowCount - 1;
    }

    @Override
    public int storeCase(CaseRecordImpl caseInput) throws Exception {
        Case caseRow = new Case(caseInput.getCaseRow());
        Log.Info(caseRow.toString());
        setRowValues(caseRowCount,
                cases,
                String.valueOf(moduleRowCount - 1),
                String.valueOf(caseRowCount),
                caseRow.getCaseName(),
                caseRow.getCaseStatus() + "");
        caseRowCount++;
        return caseRowCount - 1;
    }

    @Override
    public void storeStep(StepRecordImpl stepInput) throws Exception {
        Step stepRow = new Step(stepInput.getReportRow());
        Log.Info(stepRow.toString());
        setRowValues(stepRowCount,
                steps,
                String.valueOf(moduleRowCount - 1),
                String.valueOf(caseRowCount - 1),
                stepRow.getStepNumber() + "",
                stepRow.getAction(),
                stepRow.getDesc(),
                stepRow.getExpected(),
                stepRow.getActual(),
                stepRow.getStatus() + "",
                stepRow.getSnapShotUrl());
        stepRowCount++;
    }

    private void deleteExistingExcel() {
        File existingExcel = new File("drReporting.xls");
        if (existingExcel.exists()) {
            existingExcel.delete();
        }
    }

    private void setModuleColumnTitles() throws Exception {
        setColumnTitles(modules, "moduleId", "moduleType", "scheduler", "time", "status");
    }

    private void setCaseTitles() throws Exception {
        setColumnTitles(cases, "moduleId", "caseId", "name", "status");
    }

    private void setStepTitles() throws Exception {
        setColumnTitles(steps, "moduleId", "caseId", "stepNumber", "action", "desc", "expected", "actual", "status", "snapshotUrl");
    }

    private void setColumnTitles(HSSFSheet sheet, String... args) throws Exception {
        setRowValues(0, sheet, args);
    }

    private void setRowValues(int row, HSSFSheet sheet, String... args) throws Exception {
        try {
            this.fileOut = new FileOutputStream("drReporting.xls");
            Row rowOut = sheet.createRow(row);
            for (int i = 0; i < args.length; i++)
                rowOut.createCell(i).setCellValue(args[i]);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static HSSFWorkbook readFile(String filename) throws IOException {
        return new HSSFWorkbook(new FileInputStream(filename));
    }


//	@Override
//	public int storeModule(ModuleRecordImpl moduleInput) {
//		this.wb = new XSSFWorkbook();
//		wb.createSheet("Steps");
//		//find and store file location
//		return 0;
//	}
//
//	@Override
//	public int storeCase(CaseRecordImpl caseInput) {
//		Case caseRow = new Case(caseInput.getCaseRow());
//		Row rowInput = sheet.getRow(caseRow.getCaseId());
//		rowInput.getCell(0).setCellValue(caseRow.getCaseId());
//		rowInput.getCell(1).setCellValue(caseRow.getCaseName());
//		rowInput.getCell(2).setCellValue(caseRow.getCaseStatus());
//		return 0;
//	}
//	@Override
//	public void storeStep(StepRecordImpl stepInput) {
//		//still working on embedded image 
//		Step stepRow = new Step(stepInput.getReportRow());
//		byte[] stringBytes = stepRow.getSnapShotUrl().getBytes(Charset.forName("UTF-8"));
//		Row rowInput = sheet.getRow(stepRow.getStepId());
//		rowInput.getCell(0).setCellValue(stepRow.getStepNumber());
//		rowInput.getCell(1).setCellValue(stepRow.getAction());
//		rowInput.getCell(2).setCellValue(stepRow.getDesc());
//		rowInput.getCell(3).setCellValue(stepRow.getExpected());
//		rowInput.getCell(4).setCellValue(stepRow.getActual());
//		rowInput.getCell(5).setCellValue(stepRow.getStatus());
//		rowInput.getCell(6).setCellValue(stepRow.getSnapShotUrl());
//	}
//	
//	public void setFileName(String fileName){
//		try {
//			this.sheet = new HSSFWorkbook(new FileInputStream(new File(fileName))).getSheetAt(0);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
