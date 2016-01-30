/// <reference path="typings/main.d.ts" />

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

class ProteinValidator {

    public static Validate(input: string): string {
        let sequenceName: string;
        let start: number;
        let line: number = 1;
        let errors: Array<string> = [];
        let htmlContent: string = EmptyString;

        if (input[0] != SequenceStart) {
            errors.push("Missing &gt at descprion beginning. Unable to manage first line.");
            sequenceName = "--- no name ---"
        }
        else {
            // Get first line as description
            let nameLegth = input.indexOf(NewLine);
            sequenceName = input.substring(1, nameLegth);
            // Sequence start from next line
            start = nameLegth + 1;
            line++;

            htmlContent += "&gt" + sequenceName + HTMLNewLine;
        }

        for (var i = start, len = input.length; i < len; i++) {
            if (input[i] === SequenceStart) {
                // Search next sequence name and save it
                let nameLegth = input.indexOf(NewLine, i);
                sequenceName = input.substring(i + 1, nameLegth);
                // Sequence content start from next line
                start = nameLegth + 1;
                // Continue validation from start position
                i = start;
                line++;

                htmlContent += "&gt" + sequenceName + HTMLNewLine;
            }
            if (input[i] === NewLine) {
                line++;

                htmlContent += HTMLNewLine;
            }
            else if (input[i] != WhiteSpace && aminoAcids.indexOf(input[i]) === -1) {
                // Not found
                let error = "Invalid character '" + input[i] + "'";
                error += " in sequence '" + sequenceName;
                error += "' at line " + line;
                errors.push(error);
               
                // Use bootstrap style
                htmlContent += '<strong class="bg-info">' + input[i] + '</strong>';
            }
            else {
                htmlContent += input[i];
            }
        }

        if (errors.length == 0) {
            return null;
        }

        let result = "The errors are:";
        errors.forEach((error) => result += "<p>" + error + "</p>");
        result +=  "<p>" + htmlContent + "</p>";
        return result;
    }
}

$("#inputText").bind('input propertychange', function() {
    let inputSequences: string = $("#inputText").val();
    if (inputSequences != null &&
        inputSequences.trim() != EmptyString) {
        let htmlValidationResults = ProteinValidator.Validate(inputSequences);
        if (htmlValidationResults != null) {
            // Set error content and show
            $("#validation-errors-message").html(htmlValidationResults);
            $('#validation-errors').show();
        }
    }
});
