import csv
import argparse

def transformer_ligne(ligne):
    """Transforme une ligne du format source en format cible."""
    try:
        # Séparer la partie des motifs et celle des supports
        motifs_part, supports_part = ligne.split("#SUP:")
        
        # Nettoyage des espaces
        motifs_part = motifs_part.strip()
        supports_part = supports_part.strip()
        
        # Séparer les motifs sur " -1 " et les mettre entre parenthèses
        motifs = ["(" + part.strip() + ")" for part in motifs_part.split(" -1 ") if part]
        
        # Supprimer le motif isolé "(-2)" s'il est présent
        motifs = [motif for motif in motifs if motif != "(-2)"]
        
        # Joindre les motifs sans espace ni virgule
        motifs_str = "".join(motifs)
        
        # Séparer les supports sur ";"
        supports = supports_part.split(";")
        
        # Retourner la ligne transformée sous forme de liste
        return [motifs_str] + supports
    
    except Exception as e:
        print(f"⚠️ Erreur lors du traitement de la ligne: {ligne} - {e}")
        return None

def convertir_fichier_texte_en_csv(fichier_entree, fichier_sortie):
    """Lit un fichier texte, transforme les lignes et les enregistre dans un CSV."""
    try:
        with open(fichier_entree, "r", encoding="utf-8") as infile, open(fichier_sortie, "w", newline="", encoding="utf-8") as outfile:
            writer = csv.writer(outfile)
            
            # Écrire l'en-tête du fichier CSV
            writer.writerow(["patterns", "Supp X", "Supp Y", "Supp Z"])# patterns,Supp X,Supp Y,Supp Z
            
            for ligne in infile:
                ligne_transformee = transformer_ligne(ligne.strip())
                if ligne_transformee:
                    writer.writerow(ligne_transformee)
        
        print(f"✅ Conversion terminée ! Résultat enregistré dans {fichier_sortie}")
    
    except FileNotFoundError:
        print(f"❌ Erreur : Le fichier '{fichier_entree}' n'existe pas.")
    except Exception as e:
        print(f"❌ Erreur inattendue : {e}")

if __name__ == "__main__":
    # Définition des arguments du script
    parser = argparse.ArgumentParser(description="Convertir un fichier texte en CSV.")
    parser.add_argument("fichier_entree", help="Nom du fichier texte d'entrée")
    parser.add_argument("fichier_sortie", help="Nom du fichier CSV de sortie")
    
    # Parsing des arguments
    args = parser.parse_args()
    
    # Exécution de la conversion
    convertir_fichier_texte_en_csv(args.fichier_entree, args.fichier_sortie)

