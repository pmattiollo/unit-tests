package br.com.pmattiollo.matchers;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.com.pmattiollo.utils.DataUtils;

public class DiaDaSemanaMatcher extends TypeSafeMatcher<Date> {

	private Integer diaDaSemana;

	public DiaDaSemanaMatcher(Integer diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

	@Override
	public void describeTo(Description description) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, diaDaSemana);
		String dataPorExtenso = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
		description.appendText(dataPorExtenso);
	}

	@Override
	protected boolean matchesSafely(Date item) {
		return DataUtils.verificarDiaSemana(item, diaDaSemana);
	}

}
