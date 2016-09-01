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
        // Find all sequences
        var alignmentSequenceSections = $("." + AlignmentSequenceId);
        for (var i = 0, len = alignmentSequenceSections.length; i < len; i++) {
            var alignmentSequenceSection = $(alignmentSequenceSections[i]);
            // Save clean content
            if (AminoAcidColorsApplyer.alignmentContentNoColour.length - 1 < i) {
                AminoAcidColorsApplyer.alignmentContentNoColour.push(alignmentSequenceSection.html());
            }
            if (AminoAcidColorsApplyer.alignmentContentNoColour[i].length === 0) {
                // nothing to do
                return;
            }
            if (AminoAcidColorsApplyer.alignmentContentWithColour.length - 1 < i) {
                var alignmentContentNoColourToProcess = AminoAcidColorsApplyer.alignmentContentNoColour[i];
                // Create first element color
                var styleClass = AminoAcidColorsApplyer.getAminoClass(alignmentContentNoColourToProcess[0]);
                var htmlContent = '<span class="' + styleClass + '">' + alignmentContentNoColourToProcess[0];
                var aminoColour = {
                    htmlContent: htmlContent,
                    styleClass: styleClass
                };
                AminoAcidColorsApplyer.alignmentContentWithColour[i] = htmlContent;
                // Create colours for amino acids
                for (var j = 1, len_1 = alignmentContentNoColourToProcess.length; j < len_1; j++) {
                    aminoColour = AminoAcidColorsApplyer.applySimbolColour(alignmentContentNoColourToProcess[j], aminoColour);
                    AminoAcidColorsApplyer.alignmentContentWithColour[i] += aminoColour.htmlContent;
                }
                // Close last span
                AminoAcidColorsApplyer.alignmentContentWithColour[i] += '</span>';
            }
            // Set colour to content
            alignmentSequenceSection.html(AminoAcidColorsApplyer.alignmentContentWithColour[i]);
        }
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
            var alignmentSequenceSections = $("." + AlignmentSequenceId);
            for (var i = 0, len = alignmentSequenceSections.length; i < len; i++) {
                var alignmentSequenceSection = $(alignmentSequenceSections[i]);
                alignmentSequenceSection.html(AminoAcidColorsApplyer.alignmentContentNoColour[i]);
            }
            $("#" + ApplyAlignmentColoursButtonId).html('Show Colors');
            AminoAcidColorsApplyer.colourApplayed = false;
        }
    };
    AminoAcidColorsApplyer.getAminoClass = function (aminoAcid) {
        if (aminoAcid in AminoPolarityMap) {
            // Is an amino acid
            return AminoPolarityMap[aminoAcid];
        }
        // Isn't an amino acid
        return NotAminoAcidClas;
    };
    AminoAcidColorsApplyer.applySimbolColour = function (aminoAcid, previousAminoColour) {
        var currentStyleClass = AminoAcidColorsApplyer.getAminoClass(aminoAcid);
        if (currentStyleClass === previousAminoColour.styleClass) {
            // The current span is already correct
            return {
                htmlContent: aminoAcid,
                styleClass: currentStyleClass
            };
        }
        else {
            // Close old span and start new one
            return {
                htmlContent: '</span><span class="' + currentStyleClass + '">' + aminoAcid,
                styleClass: currentStyleClass
            };
        }
    };
    AminoAcidColorsApplyer.colourApplayed = false;
    AminoAcidColorsApplyer.alignmentContentNoColour = [];
    AminoAcidColorsApplyer.alignmentContentWithColour = [];
    return AminoAcidColorsApplyer;
}());
// Attach evet to button
$("#" + ApplyAlignmentColoursButtonId).click(function () { AminoAcidColorsApplyer.toogleColour(); });
//# sourceMappingURL=amino_colors.js.map