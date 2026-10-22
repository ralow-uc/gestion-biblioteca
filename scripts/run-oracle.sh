#!/usr/bin/env bash
# ---------------------------------------------------------------------------
#  Arranca el microservicio contra la base Oracle Cloud (perfil "oracle").
#
#  La contrasena de ADMIN NO se guarda en este archivo ni en el repositorio:
#  se lee del Llavero de macOS. Para guardarla una sola vez, ejecuta:
#
#      read -s -p "Password ADMIN: " P && \
#        security add-generic-password -a "$USER" -s fullstack3-admin -w "$P" -U && \
#        unset P && echo " guardada en el Llavero"
#
#  Uso:  ./scripts/run-oracle.sh
# ---------------------------------------------------------------------------
set -euo pipefail

# Carpeta del Wallet descomprimido (fuera del repositorio). Se puede sobrescribir
# con la variable de entorno TNS_ADMIN_DIR.
WALLET_DIR="${TNS_ADMIN_DIR:-$HOME/.oracle/wallets/FULLSTACK3}"

if [ ! -f "$WALLET_DIR/tnsnames.ora" ]; then
  echo "ERROR: no encuentro el Wallet en:" >&2
  echo "  $WALLET_DIR" >&2
  echo "Descomprime alli el Wallet_FULLSTACK3.zip o exporta TNS_ADMIN_DIR." >&2
  exit 1
fi

# IMPORTANTE: la ruta del Wallet NO puede contener espacios. El driver JDBC de
# Oracle los elimina al parsear el parametro TNS_ADMIN de la URL y la conexion
# falla con ORA-12263. Por eso el Wallet vive en ~/.oracle/wallets y no junto
# al proyecto, cuya ruta tiene espacios.

# Alias TNS. Para una carga transaccional se usa el servicio _tp.
TNS_ALIAS="${TNS_ALIAS:-fullstack3_tp}"

export ORACLE_JDBC_URL="jdbc:oracle:thin:@${TNS_ALIAS}?TNS_ADMIN=${WALLET_DIR}"
export ORACLE_USER="${ORACLE_USER:-ADMIN}"

if ! ORACLE_PASSWORD="$(security find-generic-password -a "$USER" -s fullstack3-admin -w 2>/dev/null)"; then
  echo "ERROR: no hay contrasena guardada en el Llavero con el nombre 'fullstack3-admin'." >&2
  echo "Guardala con el comando que aparece en la cabecera de este script." >&2
  exit 1
fi
export ORACLE_PASSWORD

export JAVA_HOME="${JAVA_HOME:-$(/usr/libexec/java_home -v 21)}"

echo "Conectando a ${TNS_ALIAS} como ${ORACLE_USER}"
echo "Wallet: ${WALLET_DIR}"
exec ./mvnw -q spring-boot:run -Dspring-boot.run.profiles=oracle
