package br.com.caelum.leilao.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leilao {

	private String descricao;
	private List<Lance> lances;
	
	public Leilao(String descricao) {
		this.descricao = descricao;
		this.lances = new ArrayList<Lance>();
	}
	
	public void propoe(Lance lance) {
		if (this.lances.isEmpty() || podeDarLance(lance.getUsuario())) {
			this.lances.add(lance);	
		}
	}
	
	public void dobreLance(Usuario usuario) {
		List<Lance> lancesUsuario = getLancesUsuario(usuario);
		if (lancesUsuario.size() > 0) {
			Lance ultimoLance = lancesUsuario.get(lancesUsuario.size() - 1);
			propoe(new Lance(usuario, ultimoLance.getValor() * 2));
		}
	}

	private boolean podeDarLance(Usuario usuario) {
		return !ultimoLanceDado().getUsuario().equals(usuario) && qtdLancesUsuario(usuario) < 5;
	}

	private int qtdLancesUsuario(Usuario usuario) {
		return getLancesUsuario(usuario).size();
	}

	private Lance ultimoLanceDado() {
		return this.lances.get(this.lances.size()-1);
	}

	public String getDescricao() {
		return this.descricao;
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(this.lances);
	}
	
	public List<Lance> getLancesUsuario(Usuario usuario) {
		List<Lance> lancesUsuario = new ArrayList<Lance>();
		for (Lance lance : this.lances) {
			if (lance.getUsuario().equals(usuario)) {
				lancesUsuario.add(lance);
			}
		}
		return lancesUsuario;
	}

}
