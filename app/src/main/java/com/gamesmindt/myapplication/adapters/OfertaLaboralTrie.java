package com.gamesmindt.myapplication.adapters;

import com.gamesmindt.myapplication.Model.OfertaLaboralDTO;

import java.util.ArrayList;
import java.util.List;

public class OfertaLaboralTrie {
    private TrieNode root;

    public OfertaLaboralTrie(List<OfertaLaboralDTO> ofertas) {
        root = new TrieNode();
        for (OfertaLaboralDTO oferta : ofertas) {
            insertarOferta(oferta);
        }
    }

    private void insertarOferta(OfertaLaboralDTO oferta) {
        TrieNode node = root;
        String[] palabras = obtenerPalabrasClaveOferta(oferta).split("\\s+");
        for (String palabra : palabras) {
            node = node.insertarPalabra(palabra);
        }
        node.ofertas.add(oferta);
    }

    private String obtenerPalabrasClaveOferta(OfertaLaboralDTO oferta) {
        StringBuilder sb = new StringBuilder();
        if (oferta.getNombreEmpresa() != null) {
            sb.append(oferta.getNombreEmpresa().toLowerCase()).append(" ");
        }
        if (oferta.getCargo() != null) {
            sb.append(oferta.getCargo().toLowerCase()).append(" ");
        }
        if (oferta.getAreaConocimiento() != null) {
            sb.append(oferta.getAreaConocimiento().toLowerCase()).append(" ");
        }
        if (oferta.getExperiencia() != null) {
            sb.append(oferta.getExperiencia().toLowerCase()).append(" ");
        }
        if (oferta.getTiempo() != null) {
            sb.append(oferta.getTiempo().toLowerCase()).append(" ");
        }
        if (oferta.getSalario() != 0) {
            sb.append(oferta.getSalario());
        }
        return sb.toString().trim();
    }

    public List<OfertaLaboralDTO> buscarOfertas(String prefijo) {
        TrieNode node = root;
        String[] palabras = prefijo.toLowerCase().split("\\s+");
        for (String palabra : palabras) {
            node = node.obtenerNodo(palabra);
            if (node == null) {
                return new ArrayList<>();
            }
        }
        return node.obtenerOfertas();
    }

    private static class TrieNode {
        private TrieNode[] hijos;
        private final List<OfertaLaboralDTO> ofertas;

        public TrieNode() {
            hijos = new TrieNode[26];
            ofertas = new ArrayList<>();
        }

        public TrieNode insertarPalabra(String palabra) {
            TrieNode node = this;
            for (char c : palabra.toCharArray()) {
                int index = c - 'a';
                if (index < 0 || index >= 26) {
                    // Manejar caracteres no permitidos (omitirlos o tratarlos de manera especial)
                    continue;
                }
                if (node.hijos[index] == null) {
                    node.hijos[index] = new TrieNode();
                }
                node = node.hijos[index];
            }
            return node;
        }

        public TrieNode obtenerNodo(String palabra) {
            TrieNode node = this;
            for (char c : palabra.toCharArray()) {
                int index = c - 'a';
                if (node.hijos[index] == null) {
                    return null;
                }
                node = node.hijos[index];
            }
            return node;
        }

        public List<OfertaLaboralDTO> obtenerOfertas() {
            return ofertas;
        }
    }
}
