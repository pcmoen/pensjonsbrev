package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Felles
import no.nav.pensjon.brev.maler.fraser.common.Kroner
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createPhrase
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*


val rettTilMYOverskrift_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        title1 {
            text(
                Bokmal to "For deg som har rett til minsteytelse",
                Nynorsk to "For deg som har rett til minsteyting",
                English to "You have been granted minimum benefit"
            )
        }
    }
}

val vedleggBeregnUTInfoMY_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Bor du sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,28 ganger folketrygdens grunnbeløp. Er du enslig, utgjør minste årlige ytelse 2,48 ganger folketrygdens grunnbeløp.",
            Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå den eigenopptente inntekta di er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Bur du saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane, utgjer minste årlege yting 2,28 gonger grunnbeløpet i folketrygda. Er du einsleg, utgjer minste årlege yting 2,48 gonger grunnbeløpet i folketrygda.",
            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. The minimum benefit is 2.28 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.48 times the basic amount."
        )
    }
}

val vedleggBeregnUTInfoMY2_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Bor du sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene og har uføretrygd beregnet ut fra uførepensjon per 31. desember 2014, utgjør minste årlige ytelse 2,33 ganger folketrygdens grunnbeløp. Er du enslig, utgjør minste årlige ytelse 2,48 ganger folketrygdens grunnbeløp.",
            Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå di eigenopptente inntekt er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Bur du saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane og har uføretrygd rekna ut frå uførepensjon per 31.desember 2014, utgjer minste årlege yting 2,33 gonger grunnbeløpet i folketrygda. Er du einsleg, utgjer minste årlege yting 2,48 gonger grunnbeløpet i folketrygda.",
            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. The minimum benefit is 2.33 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.48 times the basic amount."
        )
    }
}

val vedleggBeregnUTInfoMYUngUfor_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Du er innvilget rettighet som ung ufør. Minste årlige ytelse er 2,66 ganger folketrygdens grunnbeløp hvis du lever sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene. Er du enslig, utgjør minste årlige ytelse 2,91 ganger grunnbeløpet.",
            Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå di eigenopptente inntekt er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Du er innvilga rett som ung ufør. Minste årlege yting er 2,66 gonger grunnbeløpet i folketrygda dersom du lever saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane. Er du einsleg, utgjer minste årlege yting 2,91 gonger grunnbeløpet i folketrygda.",
            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. You are granted disability benefit with rights as a young disabled person. The minimum benefit is therefore 2.66 times the National Insurance basic amount if you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months. If you are single, the minimum benefit is 2.91 times the basic amount."
        )
    }
}

val vedleggBeregnUTInfoMYUngUforUnder20_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du er sikret minsteytelse fordi beregningen ut fra din egenopptjente inntekt er lavere enn minstenivået for uføretrygd. Satsen på minsteytelsen avhenger av sivilstand. Du er innvilget rettighet som ung ufør. Bor du sammen med ektefelle, partner eller i et samboerforhold som har vart i minst 12 av de siste 18 månedene, utgjør minste årlige ytelse 2,66 ganger folketrygdens grunnbeløp fra du fyller 20 år. Er du enslig, utgjør minste årlige ytelse 2,91 ganger folketrygdens grunnbeløp fra du fyller 20 år.",
            Nynorsk to "Du er sikra minsteyting fordi utrekninga ut frå di eigenopptente inntekt er lågare enn minstenivået for uføretrygd. Sats på minsteytinga er avhengig av sivilstand. Du er innvilga rett som ung ufør. Bur du saman med ektefelle, partnar eller i eit sambuarforhold som har vart i minst 12 av dei siste 18 månadane, utgjer minste årlege yting 2,66 gonger grunnbeløpet i folketrygda frå du fyller 20 år. Er du einsleg, utgjer minste årlege yting 2,91 gonger grunnbeløpet i folketrygda frå du fyller 20 år.",
            English to "You are eligible for the minimum benefit, because the calculated benefit based on your income is lower than the minimum benefit. The rate of the minimum benefit depends on your marital status. You are granted disability benefit with rights as a young disabled person. The minimum benefit is therefore 2.66 times the National Insurance basic amount if you are living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months from the age of 20. If you are single, the minimum benefit is 2.91 times the basic amount from the age of 20."
        )
    }
}

val vedleggBeregnUTDinMY_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "For deg vil minsteytelse utgjøre [_Script Tallformatering_073_] ganger folketrygdens grunnbeløp. Er uføregraden din under 100 prosent, vil minsteytelsen bli justert ut fra uføregraden. Vi justerer også minsteytelsen ut fra trygdetid hvis du har mindre enn 40 års trygdetid. Dersom sivilstanden din endrer seg, kan det medføre at uføretrygden endres.",
            Nynorsk to "For deg vil minsteytinga utgjera [_Script Tallformatering_073_] gonger grunnbeløpet i folketrygda. Er di uføregrad under 100 prosent, vil minsteytinga bli justert ut frå uføregrad. Vi justerer også minsteytinga ut frå trygdetid dersom du har mindre enn 40 års trygdetid. Dersom sivilstanden din endrar seg, kan det føre til at uføretrygda vert endra.",
            English to "For you, the minimum benefit is equal to [_Script Tallformatering_073_] times the National Insurance basic amount. If your degree of disability is lower than 100 percent, the minimum benefit will be adjusted for your degree of disability. We will also adjust the minimum benefit if your period of national insurance coverage is less than 40 years. In case of changes in your marital status your disability benefit might change."
        )
    }
}

val vedleggBeregnUTMinsteIFU_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        title1 {
            text(
                Bokmal to "Slik fastsetter vi inntekten din før du ble ufør",
                Nynorsk to "Slik fastset vi inntekta di før du blei ufør",
                English to "This is how we establish your income prior to your disability"
            )
        }
        text(
            Bokmal to "Når vi fastsetter inntekten din før du ble ufør tar vi utgangspunkt i den normale inntektssituasjonen din før du ble ufør. Denne inntekten skal likevel ikke settes lavere enn:",
            Nynorsk to "Når vi fastsett inntekta di før du blei ufør tek vi utgangspunkt i den normale inntektssituasjonen din før du blei ufør. Denne inntekta skal likevel ikkje setjast lågare enn:",
            English to "When we establish you income prior to your disability, we base our calculations on your normal income prior to your date of disability. However, your income prior to your date of disability will not be set lower than:"
        )
        list {
            item {
                text(
                    Bokmal to "3,3 ganger grunnbeløpet dersom du lever sammen med ektefelle/partner/samboer. Samboerforholdet ditt må ha vart i minst 12 av de siste 18 månedene.",
                    Nynorsk to "3,3 gonger grunnbeløpet dersom du lever saman med ektefelle/partnar/sambuar. Sambuarforholdet ditt må ha vart i minst 12 av dei siste 18 månadane.",
                    English to "3.3 times the National Insurance basic amount for individuals living with a spouse or partner, or in a cohabitant relationship that has lasted no less than 12 of the last 18 months."
                )
                text(
                    Bokmal to "3,5 ganger grunnbeløpet dersom du er enslig.",
                    Nynorsk to "3,5 gonger grunnbeløpet dersom du er einsleg.",
                    English to "3.5 times the National Insurance basic amount if you are single."
                )
            }
        }
    }
}

val slikFastsettesKompGradOverskrift_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        title1 {
            text(
                Bokmal to "Slik har vi fastsatt kompensasjonsgraden din",
                Nynorsk to "Slik har vi fastsett kompensasjonsgraden din",
                English to "This is your degree of compensation"
            )
        }
    }
}

val vedleggBeregnUTKompGrad_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vi fastsetter kompensasjonsgraden ved å sammenligne det du har rett til i 100 prosent uføretrygd med din oppjusterte inntekt før du ble ufør. Kompensasjonsgraden brukes til å beregne hvor mye vi reduserer uføretrygden din, hvis du har inntekt som er høyere enn inntektsgrensen.",
            Nynorsk to "Vi fastset kompensasjonsgrad ved å samanlikna det du har rett til i 100 prosent uføretrygd med di oppjusterte inntekt før du blei ufør. Kompensasjonsgraden vert brukt til å rekna ut kor mykje vi reduserer uføretrygda di, dersom du har inntekt som er høgare enn inntektsgrensa.",
            English to "Your degree of compensation is established by comparing what you are entitled to with a degree of disability of 100 percent, and your recalculated income prior to your disability. The degree of compensation is used to calculate how much your disability benefit will be reduced if your income exceeds the income limit."
        )
    }
}

val vedleggBeregnUTKompGradGjsntt_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig kompensasjonsgrad i beregningen.",
            Nynorsk to "Dersom uføretrygda di i løpet av eit kalenderår vert endra, bruker vi ei gjennomsnittleg kompensasjonsgrad i utrekninga.",
            English to "If your degree of compensation has changed over the course of a calendar year, your disability benefit payment will be recalculated based on your average degree of compensation."
        )
    }
}

val vedleggBeregnUTKompGradGjsnttKonvUT_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Kompensasjonsgraden skal ved beregningen ikke settes høyere enn 70 prosent. Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig kompensasjonsgrad i beregningen.",
            Nynorsk to "Kompensasjonsgraden skal ved utrekninga ikkje setjast høgare enn 70 prosent. Dersom uføretrygda di i løpet av eit kalenderår vert endra, bruker vi ei gjennomsnittleg kompensasjonsgrad i utrekning.",
            English to "Your degree of compensation will not be set higher than 70 percent. If your degree of compensation has changed over the course of a calendar year, your disability benefit payment will be recalculated based on your average degree of compensation."
        )
    }
}

val slikBeregnBTOverskrift_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        title1 {
            text(
                Bokmal to "Slik beregner vi størrelsen på barnetillegget",
                Nynorsk to "Slik reknar vi ut storleiken på barnetillegget",
                English to "How we calculate the amount of child supplement"
            )
        }
    }
}

val vedleggBeregnUTInfoBTSB_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.",
            Nynorsk to "Vi fastset storleiken på barnetillegget ut frå inntekta di. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda og det aukar med 40 prosent av grunnbeløpet for kvart ekstra barn.",
            English to "We determine the amount of child supplement based on your income. The child supplement will be reduced if your total income is greater than the exemption amount. The exemption amount is 3.1 times the National Insurance basic amount and it increases with 40 percent of the National Insurance basic amount for each extra child."
        )
    }
}

val vedleggBeregnUTredusTTBTSB_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har.",
            Nynorsk to "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har.",
            English to "As your period of national insurance cover is less than 40 years, the exemption amounts are reduced based on the period of national insurance that you have."
        )
    }
}

val vedleggBeregnUTIkkePeriodisertFriBOgInntektBTSB_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "[_Script Tallformatering_071_] kroner er 50 prosent av den inntekten som overstiger fribeløpet for barn som ikke bor sammen med begge foreldrene.",
            Nynorsk to "[_Script Tallformatering_071_] kroner er 50 prosent av den inntekta som overstig fribeløpet for barn som ikkje bur saman med begge foreldra.",
            English to "NOK [_Script Tallformatering_071_] is 50 percent of the income that exceeds the exemption amount for a child/children that does/do not live with both parents."
        )
    }
}

val vedleggBeregnUTIkkePeriodisertFriBOgInntektBTSBJusterBelop0_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "[_Script Tallformatering_071_] kroner er 50 prosent av den inntekten som overstiger fribeløpet for barn som ikke bor sammen med begge foreldrene. Dette beløpet bruker vi til å redusere barnetillegget for hele året.",
            Nynorsk to "[_Script Tallformatering_071_] kroner er 50 prosent av den inntekta som overstig fribeløpet for barn som ikkje bur saman med begge foreldra. Dette beløpet bruker vi til å redusera barnetillegget for heile året.",
            English to "NOK [_Script Tallformatering_071_] is 50 percent of the income that exceeds the exemption amount for a child/children that does/do not live with both parents. This amount will be used to reduce this child supplement during the calendar year."
        )
    }
}

val vedleggBeregnUTPeriodisertFriBOgInntektBTSBJusterBelop0_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "For barn som ikke bor sammen med begge foreldrene blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer [_Script Tallformatering_071_] kroner. Dette beløpet bruker vi til å redusere barnetillegget for hele året.",
            Nynorsk to "For barn som ikkje bur saman med begge foreldra vert 50 prosent av den inntekta som overstig fribeløpet omrekna til eit årleg beløp som svarar til [_Script Tallformatering_071_] kroner. Dette beløpet bruker vi til å redusera barnetillegget for heile året.",
            English to "For a child/children that does/do not live with both parents 50 percent of the income that exceeds the exemption amount is recalculated to an annual amount of NOK [_Script Tallformatering_071_]. This amount will be used to reduce this child supplement during the calendar year."
        )
    }
}

val vedleggBeregnUTJusterBelopOver0BTSB_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor lagt til [_Script Tallformatering_064_] kroner i beløpet vi reduserer barnetillegget med for resten av året.",
            Nynorsk to "Vi tek omsyn til korleis barnetillegget eventuelt har vore redusert tidligare, og vi har difor lagt til [_Script Tallformatering_064_]  kroner i beløpet vi reduserer barnetillegget med for resten av året.",
            English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been reduced with NOK [_Script Tallformatering_064_]."
        )
    }
}

val vedleggBeregnUTJusterBelopUnder0BTSB_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor trukket fra [_Script Tallformatering_064_] kroner i beløpet vi reduserer barnetillegget med for resten av året.",
            Nynorsk to "Vi tek omsyn til korleis barnetillegget eventuelt har vore redusert tidligare, og vi har difor trekt frå [_Script Tallformatering_064_] kroner i beløpet vi reduserer barnetillegget med for resten av året.",
            English to "We take into account how the child supplement has been reduced earlier this year. The amount with which your child supplement will be reduced for the rest of the year has therefore been reduced with NOK [_Script Tallformatering_064_]."
        )
    }
}

val reduksjonBTSBOverskrift_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        title1 {
            text(
                Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                English to "Reduction of child supplement payment for children from a previous relationship before tax"
            )
        }
    }
}

data class ReduksjonBTSBTabell1_001Dto(
    val inntektBruktIAvkortning_barnetilleggSBGjeldende: Kroner
)

val reduksjonBTSBTabell1_001 = createPhrase<LangBokmalNynorskEnglish, ReduksjonBTSBTabell1_001Dto> {
    val inntektBruktIAvkortning = argument().select(ReduksjonBTSBTabell1_001Dto::inntektBruktIAvkortning_barnetilleggSBGjeldende)
    paragraph {
        textExpr(
            Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er ".expr() + inntektBruktIAvkortning.str() + " kroner.",
            Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er ".expr() + inntektBruktIAvkortning.str() + " kroner.",
            English to "Total income applied in calculation of reduction in child supplement is NOK ".expr() + inntektBruktIAvkortning.str() + "."
        )
    }
}

data class ReduksjonBTSBTabell2_001Dto(
    val fribelop_barnetilleggSBGjeldende: Kroner
)

val reduksjonBTSBTabell2_001 = createPhrase<LangBokmalNynorskEnglish, ReduksjonBTSBTabell2_001Dto> {
    val fribelop = argument().select(ReduksjonBTSBTabell2_001Dto::fribelop_barnetilleggSBGjeldende)
    paragraph {
        textExpr(
            Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er ".expr() + fribelop.str() + " kroner.",
            Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er ".expr() + fribelop.str() + " kroner.",
            English to "Exemption amount applied in calculation of reduction in child supplement is NOK ".expr() + fribelop.str() + "."
        )
    }
}

data class ReduksjonBTSBTabell3_001Dto(
    val inntektOverFribelop_barnetilleggSBGjeldende: Kroner
)

val reduksjonBTSBTabell3_001 = createPhrase<LangBokmalNynorskEnglish, ReduksjonBTSBTabell3_001Dto> {
    val inntektOverFribelop = argument().select(ReduksjonBTSBTabell3_001Dto::inntektOverFribelop_barnetilleggSBGjeldende)
    paragraph {
        textExpr(
            Bokmal to "Inntekt over fribeløpet er ".expr() + inntektOverFribelop.str() + " kroner.",
            Nynorsk to "Inntekt over fribeløpet er ".expr() + inntektOverFribelop.str() + " kroner.",
            English to "Income exceeding the exemption amount is NOK ".expr() + inntektOverFribelop.str() + "."
        )
    }
}

data class ReduksjonBTSBTabell19_001Dto(
    val inntektstak_barnetilleggSBGjeldende: Kroner
)

val reduksjonbtsbtabell19_001 = createPhrase<LangBokmalNynorskEnglish, ReduksjonBTSBTabell19_001Dto> {
    val inntektstak = argument().select(ReduksjonBTSBTabell19_001Dto::inntektstak_barnetilleggSBGjeldende)
    paragraph {
        title1 {
            textExpr(
                Bokmal to "Grensen for å få utbetalt barnetillegg er ".expr() + inntektstak.str() + " kroner.",
                Nynorsk to "Grensa for å få utbetalt barnetillegg er ".expr() + inntektstak.str() + " kroner.",
                English to "The income limit for receiving child supplement er NOK ".expr() + inntektstak.str() + "."
            )
        }
    }
}

data class VedleggBeregnUTredusBTSBPgaInntekt_001Dto(
    val belop_barnetilleggSBGjeldende: Kroner
)

val vedleggBeregnUTredusBTSBPgaInntekt_001 = createPhrase<LangBokmalNynorskEnglish, VedleggBeregnUTredusBTSBPgaInntekt_001Dto> {
    val belop = argument().select(VedleggBeregnUTredusBTSBPgaInntekt_001Dto::belop_barnetilleggSBGjeldende)
    paragraph {
        textExpr(
            Bokmal to "Du vil få utbetalt ".expr() + belop.str() + " kroner i måneden før skatt i barnetillegg.",
            Nynorsk to "Du vil få utbetalt ".expr() + belop.str() + " kroner i månaden før skatt i barnetillegg.",
            English to "You will receive a monthly child supplement payment of NOK ".expr() + belop.str() + " before tax."
        )
    }
}

val vedleggBeregnUTIkkeUtbetaltBTSBPgaInntekt_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du får ikke utbetalt barnetillegget fordi samlet inntekt er over grensen for å få utbetalt barnetillegg.",
            Nynorsk to "Du får ikkje utbetalt barnetillegget fordi samla inntekt er over grensa for å få utbetalt barnetillegg.",
            English to "You will not receive a child supplement because your income is over the income limit for receiving a child supplement."
        )
    }
}

val vedleggBeregnUTJusterBelopIkkeUtbetalt_001 = createPhrase<LangBokmalNynorskEnglish, Unit> {
    paragraph {
        text(
            Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
            Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får difor ikkje utbetalt barnetillegg for resten av året",
            English to "You have already received what you are entitled to this year, therefore you will not receive any child supplement for the remainder of the year."
        )
    }
}
