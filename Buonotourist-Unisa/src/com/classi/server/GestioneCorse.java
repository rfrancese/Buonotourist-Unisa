package com.classi.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class GestioneCorse {
	private JSONParser jsonParser;
    
    private static String corseAndataRitornoURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
    private static String corseAndataRitorno_tag = "corseTotali";
    
    
    private static String ricercaCorseURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
    private static String ricercaCorse_tag = "cercaCorse";
    
    private static String dettaglioCorsaURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
    private static String dettaglioCorsa_tag = "dettaglioCorsa";
    
    private static String corseAndataRitornoDettagliURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
    private static String corseAndataRitornoDettagli_tag = "ARDettagli";
    
    private static String fermataVicinaURL = "http://buonotouristunisa.altervista.org/buonotourist_script/";
    private static String fermataVicina_tag = "fermataVicina";
    // constructor
    public GestioneCorse(){
        jsonParser = new JSONParser();
    }
    
    /**
     * Funzione che ritorna corse di Andata o di Ritorno
     **/
	public JSONObject caricaCorseAndataRitorno(String andataRitornoChar) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", corseAndataRitorno_tag));
        params.add(new BasicNameValuePair("andataRitorno",andataRitornoChar));
        JSONObject json = jsonParser.getJSONFromUrl(corseAndataRitornoURL, params);
        return json;
	}

	/**
     * Funzione che ricerca le corse in base ai parametri dati in input
     **/
	public JSONObject caricaCorseRicercate(String partenza,String destinazione, String orario, String andataRitorno) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag",ricercaCorse_tag));
        params.add(new BasicNameValuePair("provenienza",partenza));
        params.add(new BasicNameValuePair("destinazione",destinazione));
        params.add(new BasicNameValuePair("andataRitorno",andataRitorno));
        params.add(new BasicNameValuePair("orario",orario));
        JSONObject json = jsonParser.getJSONFromUrl(ricercaCorseURL, params);
        return json;
	}
	/**
     * Funzione che carica le fermate di una singola corsa
     **/
	public JSONObject caricaDettaglioCorsa(String codiceCorsaReale,String oraPartenzaCorsaReale, String andataRitornoCorsaReale) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag",dettaglioCorsa_tag));
        params.add(new BasicNameValuePair("codiceCorsaReale",codiceCorsaReale));
        params.add(new BasicNameValuePair("oraPartenzaCorsaReale",oraPartenzaCorsaReale));
        params.add(new BasicNameValuePair("andataRitornoCorsaReale",andataRitornoCorsaReale));
        JSONObject json = jsonParser.getJSONFromUrl(dettaglioCorsaURL, params);
        return json;
	}
	/**
     * Funzione che carica le corse di Andata o Ritorno di una specifica corsa
     **/
	public JSONObject caricaCorseAndataRitornoDettagli(String nomeCorsa,String codiceCorsaReale, String andataRitornoCorsaReale) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag",corseAndataRitornoDettagli_tag));
        params.add(new BasicNameValuePair("nomeCorsa",nomeCorsa));
        params.add(new BasicNameValuePair("codiceCorsaReale",codiceCorsaReale));
        params.add(new BasicNameValuePair("andataRitornoCorsaReale",andataRitornoCorsaReale));
        JSONObject json = jsonParser.getJSONFromUrl(corseAndataRitornoDettagliURL, params);
        return json;
	}
	/**
     * Funzione che ritorna la fermata piu' vicina in base alla latitudine e longitudine dati(CALCOLATA IN LINEA D'ARIA)
     **/
	public JSONObject trovaFermataPiuVicina(double latitudinePosition,double longitudinePosition) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag",fermataVicina_tag));
        params.add(new BasicNameValuePair("latitudine",Double.toString(latitudinePosition)));
        params.add(new BasicNameValuePair("longitudine",Double.toString(longitudinePosition)));
        JSONObject json = jsonParser.getJSONFromUrl(fermataVicinaURL, params);
        return json;
	}
}
