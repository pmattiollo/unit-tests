package br.com.pmattiollo.matchers;

import java.util.Calendar;

public class MeusMatchers {

	public static DiaDaSemanaMatcher caiEm(Integer diaDaSemana) {
		return new DiaDaSemanaMatcher(diaDaSemana);
	}

	public static DiaDaSemanaMatcher caiNumaSegunda() {
		return new DiaDaSemanaMatcher(Calendar.MONDAY);
	}

	public static DataDiferencaDiasMatcher ehHoje() {
		return new DataDiferencaDiasMatcher(0);
	}

	public static DataDiferencaDiasMatcher ehHojeComDiferencaDeDias(Integer intervalo) {
		return new DataDiferencaDiasMatcher(intervalo);
	}

}
