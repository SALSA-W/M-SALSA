===========================================================================================================================================
  M-SALSA Tests
===========================================================================================================================================

This folder contains tests code and data. In particular, *M-SALSA Tests.tar.gz* file contains:
 - BAliBASE folder: BAliBASE v3 dataset, downloaded from http://www.lbgi.fr/balibase/BalibaseDownload/BAliBASE3.0.tar.gz
 - bali_score program: source code was downloaded together with BAliBASE dataset
 - test.sh script: the script that performs test on M-SALSA performances over BAliBASE dataset.

*Please note:* the tests code has been implemented to be executed on Linux Ubuntu.

In order to execute test.sh you must have installed:
 - Clustal W
   Installation on Ubuntu: apt-get install clustalw
 - Clustal Omega
   Installation: apt-get install clustalo
 - MAFFT
   Installation: apt-get install mafft
 - MUSCLE
   Installation: apt-get install muscle
 - T-coffee
   Installation: apt-get install t-coffee
 - M-SALSA (c++ version)
   Download from: https://github.com/SALSA-W/M-SALSA
   Installation instruction: https://github.com/SALSA-W/M-SALSA/wiki/Install-instructions
 - readseq package:
   Installation: apt-get install readseq

===========================================================================================================================================

When all the required software is installed, to perform the tests open ubuntu shell:
   cd $path_of_M-SALSA_tests
   bash test.sh

The script will generate the Result folder containing the final alignments and two csv files:
 - results.csv: it contains the results of bali-score programs on all the alignments generated by the MSA tools.
 - time_and_RAM.csv: it contains informations on time and memory usage of all the MSA tools.

*Please note:* the script could need several hours to finish its job.
