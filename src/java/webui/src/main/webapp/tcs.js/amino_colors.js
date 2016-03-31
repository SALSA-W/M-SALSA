/**
 * Copyright 2016 Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/// <reference path="../typings/main.d.ts" />
"use strict";
var AlignmentSequenceId = "sequencesContent";
var ApplyAlignmentColoursButtonId = "colorsButton";
var NonPolarClass = "nonPolar";
var AcidicPolarClass = "acidicPolar";
var BasicPolarClass = "basicPolar";
var PolarClass = "polar";
var NotAminoAcidClas = "NotAA";
var AminoPolarityMap = {};
// non polar
AminoPolarityMap["A"] = NonPolarClass;
AminoPolarityMap["V"] = NonPolarClass;
AminoPolarityMap["F"] = NonPolarClass;
AminoPolarityMap["P"] = NonPolarClass;
AminoPolarityMap["M"] = NonPolarClass;
AminoPolarityMap["I"] = NonPolarClass;
AminoPolarityMap["L"] = NonPolarClass;
AminoPolarityMap["W"] = NonPolarClass;
// acidicPolar
AminoPolarityMap["D"] = AcidicPolarClass;
AminoPolarityMap["E"] = AcidicPolarClass;
// basicPolar
AminoPolarityMap["R"] = BasicPolarClass;
AminoPolarityMap["K"] = BasicPolarClass;
// polar
AminoPolarityMap["S"] = PolarClass;
AminoPolarityMap["T"] = PolarClass;
AminoPolarityMap["Y"] = PolarClass;
AminoPolarityMap["H"] = PolarClass;
AminoPolarityMap["C"] = PolarClass;
AminoPolarityMap["N"] = PolarClass;
AminoPolarityMap["G"] = PolarClass;
AminoPolarityMap["Q"] = PolarClass;
var AminoAcidColorsApplyer = (function () {
    function AminoAcidColorsApplyer() {
    }
    AminoAcidColorsApplyer.applyColour = function () {
        // Save clean content
        AminoAcidColorsApplyer.alignmentContentNoColour = $("#" + AlignmentSequenceId).html();
        if (AminoAcidColorsApplyer.alignmentContentWithColour === "") {
            // Create colours for amino acids
            for (var i = 0, len = AminoAcidColorsApplyer.alignmentContentNoColour.length; i < len; i++) {
                AminoAcidColorsApplyer.alignmentContentWithColour += AminoAcidColorsApplyer.applySimbolColour(AminoAcidColorsApplyer.alignmentContentNoColour[i]);
            }
        }
        // Set colour to content
        $("#" + AlignmentSequenceId).html(AminoAcidColorsApplyer.alignmentContentWithColour);
    };
    AminoAcidColorsApplyer.toogleColour = function () {
        if (AminoAcidColorsApplyer.colourApplayed == false) {
            // Apply colours if not applied
            AminoAcidColorsApplyer.applyColour();
            $("#" + ApplyAlignmentColoursButtonId).html('Remove Colors');
            AminoAcidColorsApplyer.colourApplayed = true;
        }
        else {
            // Restore content
            $("#" + AlignmentSequenceId).html(AminoAcidColorsApplyer.alignmentContentNoColour);
            $("#" + ApplyAlignmentColoursButtonId).html('Show Colors');
            AminoAcidColorsApplyer.colourApplayed = false;
        }
    };
    AminoAcidColorsApplyer.applySimbolColour = function (aminoAcid) {
        if (aminoAcid in AminoPolarityMap) {
            // Is an amino acid
            return '<span class="' + AminoPolarityMap[aminoAcid] + '">' + aminoAcid + '</span>';
        }
        // Isn't an amino acid
        return '<span class="' + NotAminoAcidClas + '">' + aminoAcid + '</span>';
    };
    AminoAcidColorsApplyer.colourApplayed = false;
    AminoAcidColorsApplyer.alignmentContentWithColour = "";
    return AminoAcidColorsApplyer;
}());
// Attach evet to button
$("#" + ApplyAlignmentColoursButtonId).click(function () { AminoAcidColorsApplyer.toogleColour(); });
//# sourceMappingURL=amino_colors.js.map