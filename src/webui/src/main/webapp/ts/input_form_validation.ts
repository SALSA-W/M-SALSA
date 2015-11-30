const aminoAcids: Array<string> = [
    "A",//Ala
    "C",//Cys
    "D",//Asp
    "E",//Glu
    "F",//Phe
    "G",//Gly
    "H",//His
    "I",//Ile
    "K",//Lys
    "L",//Leu
    "M",//Met
    "N",//Asn
    "P",//Pro
    "Q",//Gln
    "R",//Arg
    "S",//Ser
    "T",//Thr
    "V",//Val
    "W",//Trp
    "Y",//Tyr
];

const NewLine: string = "\n";
const EmptyString: string = "";
const WhiteSpace: string = " ";
const HTMLNewLine: string = "<br>";

class ProteinValidator {
    private sequence: string;
    private description: string;
    private htmlSequence: string = EmptyString;
    private errors: Array<string> = [];

    constructor(input: string) {
        if (input[0] != ">") {
            this.errors.push("Missing > at descprion beginning. Unable to manage first line.");
            this.sequence = input;
        }
        else {
            // Get first line as description
            let firstNewLineIndex = input.indexOf(NewLine);
            this.description = input.substring(0, firstNewLineIndex);
            // Use the rest of string as sequence
            this.sequence = input.substring(firstNewLineIndex + 1, input.length);
        }

        for (var i = 0, len = this.sequence.length; i < len; i++) {
            if (this.sequence[i] === NewLine) {
                this.htmlSequence += HTMLNewLine;
            }
            else {
                // Skip white space from validation
                if (this.sequence[i] != WhiteSpace) {
                    if (aminoAcids.indexOf(this.sequence[i]) === -1) {
                        // Not found
                        this.htmlSequence += this.AddHTMLErrorDecorator(this.sequence[i]);
                        this.errors.push("Invalid character " + this.sequence[i]);
                    }
                    else {
                        this.htmlSequence += this.sequence[i];
                    }
                }
            }
        }
    }

    private AddHTMLErrorDecorator(invalidCharacter: string): string {
        // Use bootstrap style
        return '<strong class="text-danger">' + invalidCharacter + '</strong>';
    }

    public static ComposeErrorDiv(proteinValidator: ProteinValidator): string {
        if (proteinValidator.errors.length == 0) {
            return null;
        }

        let result = '<div class="alert"><strong class="text-danger">Validation Errors!</strong>' + HTMLNewLine;
        result += '<p>In sequence <strong>' + proteinValidator.description + '</strong></p>';
        result += proteinValidator.htmlSequence;
        result += '<div class="alert alert-danger">The errors are:'
        proteinValidator.errors.forEach((error) => result += HTMLNewLine + error);
        // Alert Error div
        result += "</div>";
        // Alert div
        result += "</div>";
        return result;
    }
}

// TEST CODE
let inputProt = `>tr|A6QQ10|A6QQ10_BOVIN ACD protein (Fragment) OS=Bos taurus GN=ACD PE=2 SV=1
    DVRRFPSSGEAGPRRTGKTRARGYLRSPAAGMASFGGLVLRPWIRELVLGSDALSSPRAG
    QLLKVLQEAKAQSPSGAPDPPDAEAMLLVSDGTHSIRCLVTGEALNASDWEEKEFGFRGT
    EGRILLLRDCKVSVQVAQGDTPAEFYLQVDRFALLPTEQPREQVTGCNEDPDVRKKLCDC
    LEEHLSESTSSNTGLSLSQLLNEVEVDQEHQKALVRLAESCLILAGPDTAAPITPWAASR
    CRATGEAVYTVPSLRLHISENDQQILSSLGPTQRVQGPERSPSHLALQDLSLSLISSPPS
    SPSSSGTPAIPSHLLSKENSASVSLLPPLPLAAPDPVQKGSSQPLSTICSAHGSLPPGSP
    HPSSIPNTPLLSCTPSLSRLGHAPSIHQAHVSRAQKPSLEFKELGLPPKTLHHSPRTRTT
    KGALESòSCVWDPPERHRDGSAFQYEYRPPCPSLCAQVQAARLPPQLVAWALHFLMEPQPD
    SELTQM`;
let proteinValidator = new ProteinValidator(inputProt);
let validationResults = ProteinValidator.ComposeErrorDiv(proteinValidator);