import pandas as pd
import os
import argparse

def transformer_fichier(fichier_entree, dossier_sortie):
    """ Transforme un fichier CSV et l'enregistre dans le dossier de sortie """
    
    # Chargement du fichier CSV
    df = pd.read_csv(fichier_entree)
    
    # Vérification de la présence de la colonne CountryCode
    if "CountryCode" not in df.columns:
        print(f"⚠️ Fichier {fichier_entree} ignoré (pas de colonne 'CountryCode').")
        return
    
    # Dictionnaire de remplacement des années
    annees_mapping = {year: idx+1 for idx, year in enumerate(range(2015, 2025))}
    
    # Remplacement des années
    df["Year"] = df["Year"].replace(annees_mapping)
    
    # Colonnes à supprimer
    colonnes_a_supprimer = ["CountryCode", "Sex", "Split", "SplitSex", "Forecast","Total.1"]
    
    # Supprimer toutes les colonnes "Total" présentes
    colonnes_total = [col for col in df.columns if col == "Total"]
    colonnes_a_supprimer.extend(colonnes_total)
    
    # Suppression des colonnes
    df_transforme = df.drop(columns=[col for col in colonnes_a_supprimer if col in df.columns])
    
    # Détermination du nom de fichier de sortie
    country_code = df["CountryCode"].iloc[0]
    nom_fichier_sortie = f"{country_code}.csv"
    
    # Création du dossier de sortie s'il n'existe pas
    os.makedirs(dossier_sortie, exist_ok=True)
    
    # Sauvegarde du fichier transformé dans le dossier de sortie
    chemin_sortie = os.path.join(dossier_sortie, nom_fichier_sortie)
    df_transforme.to_csv(chemin_sortie, index=False)
    
    print(f"✅ {fichier_entree} → {chemin_sortie}")

def traiter_repertoire(repertoire_courant, dossier_sortie):
    """ Parcourt tous les fichiers CSV du répertoire courant et applique la transformation """
    fichiers_csv = [f for f in os.listdir(repertoire_courant) if f.endswith(".csv")]
    
    if not fichiers_csv:
        print("⚠️ Aucun fichier CSV trouvé dans le répertoire courant.")
        return
    
    for fichier in fichiers_csv:
        transformer_fichier(fichier, dossier_sortie)

if __name__ == "__main__":
    # Configuration des arguments du script
    parser = argparse.ArgumentParser(description="Transformer tous les fichiers CSV du répertoire courant.")
    parser.add_argument("--out", default="out", help="Nom du dossier de sortie (défaut: 'out')")
    
    # Parsing des arguments
    args = parser.parse_args()
    
    # Exécution de la transformation sur tous les fichiers du répertoire courant
    traiter_repertoire(".", args.out)
