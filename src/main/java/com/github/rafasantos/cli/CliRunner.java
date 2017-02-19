package com.github.rafasantos.cli;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.github.rafasantos.context.ContextProvider;
import com.github.rafasantos.controller.DiffController;
import com.github.rafasantos.pojo.LinePojo;
import com.github.rafasantos.ui.ConsoleUi;

public class CliRunner {

	private DiffController fileController = ContextProvider.getDiffController();

	public void run(AppCliHandler cli) {
		FileInputStream ffis = null;
		FileInputStream sfis = null;
		List<LinePojo> response = null;
		try {
			ffis = new FileInputStream(cli.getFirstFile());
			BufferedReader firstFileReader = new BufferedReader(new InputStreamReader(ffis));
			sfis = new FileInputStream(cli.getSecondFile());
			BufferedReader secondFileReader = new BufferedReader(new InputStreamReader(sfis));
			response = fileController.getDiff(firstFileReader, secondFileReader, cli.getUniqueIndexes(), cli.getTextDelimiter(), cli.getEqualsTemplate(),
					cli.getInsertTemplate(), cli.getUpdateTemplate(), cli.getDeleteTemplate());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (ffis != null) {
					ffis.close();
				}
				if (sfis != null) {
					sfis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(LinePojo l : response) {
			if (l.getDiffType() == LinePojo.DiffType.DELETED) {
				ConsoleUi.printRed(l.getDiffText());
			} else if (l.getDiffType() == LinePojo.DiffType.UPDATED) {
				ConsoleUi.printYellow(l.getDiffText());
			} else if (l.getDiffType() == LinePojo.DiffType.INSERTED) {
				ConsoleUi.printGreen(l.getDiffText());
			} else {
				ConsoleUi.print(l.getDiffText());
			}
		}
		
	}

}