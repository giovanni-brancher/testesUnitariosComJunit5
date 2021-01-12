package br.com.caelum.leilao.parametrizados;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Month;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.StringUtils;

public class ParametrizadosTest {

	@ParameterizedTest
	@EnumSource(Month.class)
	void deveValidarNumeroCorrespondenteMesTest(Month mes) {
	    int numeroMes = mes.getValue();
	    assertTrue(numeroMes >= 1 && numeroMes <= 12);
	}
	
	@ParameterizedTest(name = "{0} possui 30 dias.")	
	@EnumSource(value = Month.class, names = {"APRIL", "JUNE", "SEPTEMBER", "NOVEMBER"})
	void deveValidarMesesCom30DiasTest(Month mes) {
	    final boolean anoBissexto = false;
	    assertEquals(30, mes.length(anoBissexto));
	}
	
	@ParameterizedTest
	@CsvSource({"test,TEST", "tEst,TEST", "Java,JAVA"})
	void deveValidarTextoEmMaiusculoTest(String texto, String expectativa) {
	    String textoAtual = texto.toUpperCase();
	    assertEquals(expectativa, textoAtual);
	}
	
	private static Stream<Arguments> deveValidarTextoEmBrancoTest() {
		return Stream.of(
				Arguments.of(null, true),
				Arguments.of("", true),
				Arguments.of("  ", true),
				Arguments.of("não branco", false)
				);
	}
	
	@ParameterizedTest
	@MethodSource("deveValidarTextoEmBrancoTest")
	void isBlank_ShouldReturnTrueForNullOrBlankStrings(String texto, boolean expectativa) {
	    assertEquals(expectativa, StringUtils.isBlank(texto));
	}
	
}
