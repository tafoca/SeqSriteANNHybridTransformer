import pandas as pd
import os
import argparse

def traiter_fichier(fichier_entree, dossier_sortie):
    """ Transforme un fichier CSV en supprimant l'entête et filtrant les lignes sur la première colonne """
    
    # Lecture du fichier CSV sans la première ligne (entête)
    df = pd.read_csv(fichier_entree, skiprows=1, header=None)
    
    # Filtrage : garder uniquement les lignes où la première colonne est entre 1 et 10
    df_filtre = df[(df.iloc[:, 0] >= 1) & (df.iloc[:, 0] <= 10)]
    
    # Création du dossier de sortie s'il n'existe pas
    os.makedirs(dossier_sortie, exist_ok=True)
    
    # Remplacement des virgules par des espaces et sauvegarde du fichier
    fichier_sortie = os.path.join(dossier_sortie, fichier_entree)
    df_filtre.to_csv(fichier_sortie, index=False, header=False, sep=" ")
    
    print(f"✅ {fichier_entree} → {fichier_sortie}")

def traiter_repertoire(repertoire_courant, dossier_sortie):
    """ Applique la transformation à tous les fichiers CSV du répertoire courant """
    fichiers_csv = [f for f in os.listdir(repertoire_courant) if f.endswith(".csv")]
    
    if not fichiers_csv:
        print("⚠️ Aucun fichier CSV trouvé dans le répertoire courant.")
        return
    
    for fichier in fichiers_csv:
        traiter_fichier(fichier, dossier_sortie)

if __name__ == "__main__":
    # Configuration des arguments du script
    parser = argparse.ArgumentParser(description="Traitement des fichiers CSV du répertoire courant.")
    parser.add_argument("--out", default="out", help="Nom du dossier de sortie (défaut: 'out')")
    
    # Parsing des arguments
    args = parser.parse_args()
    
    # Exécution de la transformation
    traiter_repertoire(".", args.out)

