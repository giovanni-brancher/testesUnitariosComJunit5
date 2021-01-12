package br.com.caelum.leilao.servico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;

public class Avaliador {

	private double maiorValorDeTodos = Double.NEGATIVE_INFINITY;
	private double menorValorDeTodos = Double.POSITIVE_INFINITY;
	private double somatorioLances = 0f;
	private double mediaDosLances = 0f;
	private List<Lance> maioresLances;

	public void avalia(Leilao leilao) {
		if (leilao.getLances().size() == 0) {
			throw new RuntimeException("Não é possível avaliar um leilão sem lances.");
		}
		
		for (Lance lance : leilao.getLances()) {
			if (lance.getValor() > maiorValorDeTodos) {
				maiorValorDeTodos = lance.getValor();
			}
			
			if (lance.getValor() < menorValorDeTodos) {
				menorValorDeTodos = lance.getValor();
			}
			
			somatorioLances  += lance.getValor();
		}
		
		mediaDosLances  = somatorioLances / leilao.getLances().size();
		maioresLances = ordenarLancesCrescente(leilao.getLances());
	}
	
	private List<Lance> ordenarLancesCrescente(List<Lance> lances) {
		lances = new ArrayList<Lance>(lances);
		
		Collections.sort(lances, new Comparator<Lance>() {
			public int compare(Lance o1, Lance o2) {
				if (o1.getValor() < o2.getValor()) return 1;
				if (o1.getValor() > o2.getValor()) return -1;
				return 0;
			}
		});
	
		return lances;
	}
	
	public double getMediaDosLances() {
		return mediaDosLances;
	}
	
	public double getMaiorLance() {
		return maiorValorDeTodos;
	}
	
	public double getMenorLance() {
		return menorValorDeTodos;
	}
	
	public List<Lance> getMaioresLances() {
		Integer tamanho = maioresLances.size() > 3 ? 3 : maioresLances.size();
		maioresLances = maioresLances.subList(0, tamanho);
		return maioresLances;
	}
	
}
