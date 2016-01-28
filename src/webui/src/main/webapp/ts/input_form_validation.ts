/// <reference path="typings/jquery/jquery.d.ts" />

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
const SequenceStart: string = ">";

class SequenceError {
    public Line: number;
    public Message: string;

    constructor(line: number, message: string) {
        this.Line = line;
        this.Message = message;
    }
}

class Sequence {
    public Name: string;
    public Content: string;

    constructor(name: string, content: string) {
        this.Name = name;
        this.Content = content;
    }
}

class ProteinValidator {

    public static Validate(input: string) : string {
        let sequenceName: string;
        let start: number;
        let line: number = 1;
        let sequences: Array<Sequence> = [];
        let errors: Array<SequenceError> = [];
        let htmlContent: string = EmptyString;

        if (input[0] != SequenceStart) {
            errors.push(new SequenceError(0, "Missing > at descprion beginning. Unable to manage first line."));
            sequenceName = "--- no name ---"
        }
        else {
            // Get first line as description
            let nameLegth = input.indexOf(NewLine);
            sequenceName = input.substring(1, nameLegth);
            // Sequence start from next line
            start = nameLegth + 1;
            line++;
            
            htmlContent += ">" + sequenceName + HTMLNewLine;
        }

        for (var i = start, len = input.length; i < len; i++) {
            if (input[i] === SequenceStart) {
                sequences.push(new Sequence(sequenceName, input.substring(start, i)));
                // Search next sequence name and save it
                let nameLegth = input.indexOf(NewLine, i);
                sequenceName = input.substring(i + 1, nameLegth);
                // Sequence content start from next line
                start = nameLegth + 1;
                // Continue validation from start position
                i = start;
                line++;
                
                htmlContent += ">" + sequenceName + HTMLNewLine;
            }
            if (input[i] === NewLine) {
                line++;
                
                htmlContent += HTMLNewLine;
            }
            else if (input[i] != WhiteSpace && aminoAcids.indexOf(input[i]) === -1) {
                // Not found
                let errorMessage = "Invalid character '" + input[i] + "'";
                errorMessage += " in sequence '" + sequenceName;
                errorMessage += "' at line " + line;
                errors.push(new SequenceError(line, errorMessage));
               
                // Use bootstrap style
                htmlContent += '<strong class="text-danger">' + input[i] + '</strong>';
            }
            else {
                htmlContent += input[i];
            }
        }
        
        // Add last sequence
        sequences.push(new Sequence(sequenceName, input.substring(start, input.length)));
        
        if (errors.length == 0) {
            return null;
        }

        let result = '<div class="alert"><strong class="text-danger">Validation Errors!</strong>' + HTMLNewLine;
        result += htmlContent;
        result += '<div class="alert alert-danger">The errors are:'
        errors.forEach((error) => result += HTMLNewLine + error);
        // Alert Error div
        result += "</div>";
        // Alert div
        result += "</div>";
        
        return result;
    }
}


// TEST CODE
let inputProt = `>tr|A6QQ10|A6QQ10_BOVIN ACD protein 1(Fragment) OS=Bos taurus GN=ACD PE=2 SV=1
    DVRRFPSSGEAGPRRTGKTRARGYLRSPAAGMASFGGLVLRPWIRELVLGSDALSSPRAG
    QLLKVLQEAKAQSPSGAPDPPDAEAMLLVSDGTHSIRCLVTGEALNASDWEEKEFGFRGT
    EGRILLLRDCKVSVQVAQGDTPAEFYLQVDRFALLPTEQPREQVTGCNEDPDVRKKLCDC
    LEEHLSESTSSNTGLSLSQLLNEVEVDQEHQKALVRLAESCLILAGPDTAAPITPWAASR
    CRATGEAVYTVPSLRLHISENDQQILSSLGPTQRVQGPERSPSHLALQDLSLSLISSPPS
    SPSSSGTPAIPSHLLSKENSASVSLLPPLPLAAPDPVQKGSSQPLSTICSAHGSLPPGSP
    HPSSIPNTPLLSCTPSLSRLGHAPSIHQAHVSRAQKPSLEFKELGLPPKTLHHSPRTRTT
    KGALESòSCVWDPPERHRDGSAFQYEYRPPCPSLCAQVQAARLPPQLVAWALHFLMEPQPD
    SELTQM
    >tr|A6QQ10|A6QQ10_BOVIN ACD protein 2(Fragment) OS=Bos taurus GN=ACD PE=2 SV=1
    DVRRFPSSGEAGPRRTGKTRARGYLRSPAAGMASFGGLVLRPWIRELVLGSDALSSPRAG
    QLLKVLQEAKAQSPSGAPDPPDAEAMLLVSDGTHSIRCLVTGEALNASDWEEKEFGFRGT
    EGRILLLRDCKVSVQVAQGDTPAEFYLQVDRFALLPTEQPREQVTGCNEDPDVRKKLCDC
    LEEHLSESTSSNTGLSLSQLLNEVEVDQEHQKALVRLAESCLILAGPDTAAPITPWAASR
    CRATGEAVYTVPSLRLHISENDQQILSSLGPTQRVQGPERSPSHLALQDLSLSLISSPPS
    SPSSSGTPAIPSHLLSKENSASVSLLPPLPLAAPDPVQKGSSQPLSTICSAHGSLPPGSP
    HPSSIPNTPLLSCTPSLSRLGHAPSIHQAHVSRAQKPSLEFKELGLPPKTLHHSPRTRTT
    KGALES%SCVWDPPERHRDGSAFQYEYRPPCPSLCAQVQAARLPPQLVAWALHFLMEPQPD
    SELTQM
    >tr|A6QQ10|A6QQ10_BOVIN ACD protein 3(Fragment) OS=Bos taurus GN=ACD PE=2 SV=1
    DVRRFPSSGEAGPRRTGKTRARGYLRSPAAGMASFGGLVLRPWIRELVLGSDALSSPRAG
    QLLKVLQEAKAQSPSGAPDPPDAEAMLLVSDGTHSIRCLVTGEALNASDWEEKEFGFRGT
    EGRILLLRDCKVSVQVAQGDTPAEFYLQVDRFALLPTEQPREQVTGCNEDPDVRKKLCDC
    LEEHLSESTSSNTGLSLSQLLNEVEVDQEHQKALVRLAESCLILAGPDTAAPITPWAASR
    CRATGEAVYTVPSLRLHISENDQQILSSLGPTQRVQGPERSPSHLALQDLSLSLISSPPS
    SPSSSGTPAIPSHLLSKENSASVSLLPPLPLAAPDPVQKGSSQPLSTICSAHGSLPPGSP
    HPSSIPNTPLLSCTPSLSRLGHAPSIHQAHVSRAQKPSLEFKELGLPPKTLHHSPRTRTT
    KGALES?SCVWDPPERHRDGSAFQYEYRPPCPSLCAQVQAARLPPQLVAWALHFLMEPQPD
    SELTQM
    >tr|A6QQ10|A6QQ10_BOVIN ACD protein 4(Fragment) OS=Bos taurus GN=ACD PE=2 SV=1
    DVRRFPSSGEAGPRRTGKTRARGYLRSPAAGMASFGGLVLRPWIRELVLGSDALSSPRAG
    QLLKVLQEAKAQSPSGAPDPPDAEAMLLVSDGTHSIRCLVTGEALNASDWEEKEFGFRGT
    EGRILLLRDCKVSVQVAQGDTPAEFYLQVDRFALLPTEQPREQVTGCNEDPDVRKKLCDC
    LEEHLSESTSSNTGLSLSQLLNEVEVDQEHQKALVRLAESCLILAGPDTAAPITPWAASR
    CRATGEAVYTVPSLRLHISENDQQILSSLGPTQRVQGPERSPSHLALQDLSLSLISSPPS
    SPSSSGTPAIPSHLLSKENSASVSLLPPLPLAAPDPVQKGSSQPLSTICSAHGSLPPGSP
    HPSSIPNTPLLSCTPSLSRLGHAPSIHQAHVSRAQKPSLEFKELGLPPKTLHHSPRTRTT
    KGALES^SCVWDPPERHRDGSAFQYEYRPPCPSLCAQVQAARLPPQLVAWALHFLMEPQPD
    SELTQM`;
    
let htmlValidationResults = ProteinValidator.Validate(inputProt);