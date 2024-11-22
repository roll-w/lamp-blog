#!/bin/sh

#
# Copyright (C) 2023 RollW
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Startup script expects a folder structure like:
# .
# |- bin
#     |- lampray
# |- lib
#     |- lampray.jar
#

# Resolve links: $0 may be a link
app_path=$0

# Need this for daisy-chained symlinks.
while
  APP_HOME=${app_path%"${app_path##*/}"} # leaves a trailing /; empty if no leading path
  [ -h "$app_path" ]
do
  ls=$(ls -ld "$app_path")
  link=${ls#*' -> '}
  case $link in         #(
    /*) app_path=$link ;; #(
    *) app_path=$APP_HOME$link ;;
  esac
done

warn() {
  echo "$*"
} >&2

die() {
  echo
  echo "$*"
  echo
  exit 1
} >&2

# OS specific support (must be 'true' or 'false').
# shellcheck disable=SC2034
cygwin=false
msys=false
darwin=false
nonstop=false
case "$(uname)" in           #(
  CYGWIN*) cygwin=true ;;      #(
  Darwin*) darwin=true ;;      #(
  MSYS* | MINGW*) msys=true ;; #(
  NONSTOP*) nonstop=true ;;
esac

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ]; then
  if [ -x "$JAVA_HOME/jre/sh/java" ]; then
    # IBM's JDK on AIX uses strange locations for the executables
    JAVACMD=$JAVA_HOME/jre/sh/java
  else
    JAVACMD=$JAVA_HOME/bin/java
  fi
  if [ ! -x "$JAVACMD" ]; then
    die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
  fi
else
  JAVACMD=java
  if ! command -v java >/dev/null 2>&1; then
    die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
  fi
fi

# For Cygwin or MSYS, switch paths to Windows format before running java
if "$cygwin" || "$msys"; then
  JAVACMD=$(cygpath --unix "$JAVACMD")

  # Now convert the arguments - kludge to limit ourselves to /bin/sh
  for arg; do
    if
      case $arg in #(
      -*) false ;; # don't mess with options #(
      /?*)
        t=${arg#/} t=/${t%%/*} # looks like a POSIX filepath
        [ -e "$t" ]
        ;; #(
      *) false ;;
      esac
    then
      arg=$(cygpath --path --ignore --mixed "$arg")
    fi
    # Roll the args list around exactly as many times as the number of
    # args, so each arg winds up back in the position where it started, but
    # possibly modified.
    #
    # NB: a `for` loop captures its iteration list before it begins, so
    # changing the positional parameters here affects neither the number of
    # iterations, nor the values presented in `arg`.
    shift              # remove old arg
    set -- "$@" "$arg" # push replacement arg
  done
fi

cd "$APP_HOME.." || die "Cannot enter working directory."

LAMP_JAR="./lib/lampray.jar"

if [ ! -f "$LAMP_JAR" ]; then
  die "Error: '$LAMP_JAR' does not exist.
Please verify that this is a standard release package."
fi

# Add default JVM options here. You can also use JAVA_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx512m" "-Xms64m"'

# Stop when "xargs" is not available.
if ! command -v xargs >/dev/null 2>&1; then
  die "xargs is not available"
fi

JAVA_OPTS=$(
  printf '%s\n' "$DEFAULT_JVM_OPTS $JAVA_OPTS" |
    xargs -n1 |
    sed ' s~[^-[:alnum:]+,./:=@_]~\\&~g; ' |
    tr '\n' ' '
)

# shellcheck disable=SC2086
# disable SC2086: Since sh not support array, so we use unquoted $JAVA_OPTS here.
exec "$JAVACMD" $JAVA_OPTS -jar "$LAMP_JAR" "$@"
