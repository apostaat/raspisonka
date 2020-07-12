(ns raspisonka.core
  (:require [clojure.string :as str]
            [environ.core :refer [env]]
            [morse.handlers :as h]
            [morse.polling :as p]
            [morse.api :as t]
            [cheshire.core :refer :all :as json]
            [send-mail :as g ]
            [hiccup.core :as hiccup]
            [clojure.java.io :as io]
            [clojure.core.async :refer [<!!]]
            [emoji.core :as e]
            [clj-htmltopdf.core :as pdf]
            [clj-time.core :as time]
            [clj-time.local :as loc]
            [clj-time.format :as form])
  (:gen-class))

(def token (env :telegram-token))
(def base (agent {}))
(def base2 (atom (apply conj {} (map-indexed (fn [ind itm] {(keyword (str "q" ind)) itm}) (range 27)))))

(defn sm
  [code]
  (-> code Character/toChars String.))

(defn month-convert [month]
  (case month
    "01" " января "
    "02" " февраля "
    "03" " марта "
    "04" " апреля "
    "05" " мая "
    "06" " июня "
    "07" " июля "
    "08" " августа "
    "09" " сентября "
    "10" " октября "
    "11" " ноября "
    "12" " декабря "))

(defn time-string []
  (let [y (form/unparse (form/formatter "yyyy") (loc/local-now))
        m (month-convert (form/unparse(form/formatter "MM") (loc/local-now)))
        d (form/unparse (form/formatter "dd") (loc/local-now))]
    (str d m y " года")))

(defn profile-pdf-stream-test []
  (let [baos (java.io.ByteArrayOutputStream.)]
    (pdf/->pdf
      [:html {:lang "en"}
       [:head
        [:meta {:charset "UTF-8"}]
        [:title "Speaker Submission"]
        [:style {:type "text/css"} ".block0 {
       width: 650px;
       background: #4758a5;
       padding: 5px;
       padding-right: 20px;
       border: solid 0px black;
      }
      .block1 {
       margin-left: 100px;
       width: 400px;
       background: #ffffff;
       padding: 5px;
       padding-right: 20px;
       border: solid 2px black;
       position: absolute;
       text-align: center;
       font-weight: bold;
       border-top: 0px;
       margin-top: -0.3em;
      }
      .block-in {
        display: inline;
        font-weight: normal;
      }
      .block-in2 {
        display: inline;
        font-weight: normal;
      }
      .block2 {
       margin-top: 100px;
       margin-right: auto;
       margin-left: -0.3em;
       width: 600px;
       background: #ffdc38;
       padding: 5px;
       padding-right: 20px;
       border: solid 2px black;
       align: left;
       position: relative;
       border-left: 0px;
      }
      .block3 {
       margin-top: 35px;
       margin-left: auto;
       margin-right: -1.2em;
       width: 600px;
       background: #ffdc38;
       padding: 5px;
       padding-right: 20px;
       border: solid 2px black;
       border-right: 0px;

      }
      .block4 {
       margin-top: 30px;
       margin-left: 50px;
       margin-bottom: -5px;
       width: 540px;
       background: #ffffff;
       padding: 5px;
       padding-right: 20px;
       padding-bottom: 10px;
       border: solid 2px black;
       border-bottom: 0px;
       position: relative;
      }
       .block5 {
       border: 0px;}

      .inside-text {
        font-weight: bold;

      }
      .inside-image{
        float: right;
      }

      #left { align: left; }
      #right { align: right; }
      #center { align: center; }"]]
       [:body
        [:div.block0
         [:div.block1 [:p#center "ДОГОВОР ЗАЙМА"]
          [:div.block-in  (str (:q26 @base2) "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;")]
          [:div.block-in (str "&nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;" (time-string) )]]
         [:div.block2
          [:ul {:style "list-style-type: none;"}
           [:li [:span.inside-text "Заёмщик:"] (str (:q0  @base2))]
           [:li [:span.inside-text "дата рождения:"] (str (:q1 @base2))]
           [:li [:span.inside-text "место рождения:"] (str (:q2 @base2))]
           [:li [:span.inside-text "гражданство:"] (str (:q3 @base2))]
           [:li [:span.inside-text "пол:"] (str (:q4 @base2))]
           [:li [:span.inside-text "паспорт (серия и номер):"] (str (:q5 @base2))]
           [:li [:span.inside-text "выдан:"] (str (:q6 @base2))]
           [:li [:span.inside-text "код подразделения:"] (str (:q7 @base2))]
           [:li [:span.inside-text "адрес регистрации:"] (str (:q8 @base2))]
           [:li [:span.inside-text "номер телефона:"] (str (:q9 @base2))]
           (when-not (= 0 (:q10 @base2)) [:li [:span.inside-text "ИНН:"] (str (:q10 @base2))] )
           (when-not (= 0 (:q11 @base2)) [:li [:span.inside-text "СНИЛС:"] (str (:q11 @base2))] )
           [:li [:span.inside-text "электронная почта:"] (str (:q12 @base2))]]]
         [:div [:img {:src "hands02.png" :alt "" :style "float: right;" :width "150"}]]
         [:div [:img.inside-image {:src "hands01.png" :alt "" :width "150"}]]
         [:div.block3
          [:ul {:style "list-style-type: none;"}
           [:li [:span.inside-text "Займодавец:"] (str (:q13 @base2))]
           [:li [:span.inside-text "дата рождения:"] (str (:q14 @base2))]
           [:li [:span.inside-text "место рождения:"]  (str (:q15 @base2))]
           [:li [:span.inside-text "гражданство:"] "РФ" (str (:q16 @base2))]
           [:li [:span.inside-text "пол:"] (str (:q17 @base2))]
           [:li [:span.inside-text "паспорт (серия и номер):"] (str (:q18 @base2))]
           [:li [:span.inside-text "выдан:"] (str (:q20 @base2))]
           [:li [:span.inside-text "код подразделения:"] (str (:q19 @base2))]
           [:li [:span.inside-text "адрес регистрации:"] (str (:q20 @base2))]
           [:li [:span.inside-text "номер телефона:"] (str (:q21 @base2))]
           [:li [:span.inside-text "электронная почта:"] (str (:q22 @base2))]]]
         [:div.block4
          [:div [:img {:src "hands03.png" :alt "" :style "float: left;" :width "150"}]]
          [:div [:img {:src "hands04.png" :alt "" :style "float: right;" :width "150"}]]
          [:p (str "Подписывая настоящий договор стороны признают, что на момент его подписания
          займодавец передал заёмщику, а заёмщик получил в долг от займодавца
           денежные средства в сумме " (str (:q23 @base2))  "на следующих условиях:")]
          [:ul {:style "list-style-type: none; list-style-position: inside; text-align: center; "}
           [:li [:span.inside-text "проценты:"] (if (= 0 (:q24 @base2))
                                                  "беспроцентный"
                                                  (str (:q24 @base2) "%") )]
           [:li [:span.inside-text "дата возврата:"] (if (= 0 (:q25 @base2))
                                                       "до востребования"
                                                       (str (:q25 @base2)))]]
          [:table
           [:tr
            [:th "Заёмщик"]
            [:th "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"]
            [:th "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"]
            [:th "Займодавец"]] [:tr [:td "________________"]
                                 [:td "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"]
                                 [:td "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"]
                                 [:td "________________"]]]]]]] baos {:page {:size :A4 :margin "27mm 16mm 27mm 16mm" }})
    (.toByteArray baos)))

(defn profile-pdf-stream [id]
  (let [baos (java.io.ByteArrayOutputStream.)
        k    (keyword (str id))]
    (pdf/->pdf
      [:html {:lang "en"}
       [:head
        [:meta {:charset "UTF-8"}]
        [:title "Speaker Submission"]
        [:style {:type "text/css"} ".block0 {
       width: 650px;
       background: #4758a5;
       padding: 5px;
       padding-right: 20px;
       border: solid 0px black;
      }
      .block1 {
       margin-left: 100px;
       width: 400px;
       background: #ffffff;
       padding: 5px;
       padding-right: 20px;
       border: solid 2px black;
       position: absolute;
       text-align: center;
       font-weight: bold;
       border-top: 0px;
       margin-top: -0.3em;
      }
      .block-in {
        display: inline;
        font-weight: normal;
      }
      .block-in2 {
        display: inline;
        font-weight: normal;
      }
      .block2 {
       margin-top: 100px;
       margin-right: auto;
       margin-left: -0.3em;
       width: 600px;
       background: #ffdc38;
       padding: 5px;
       padding-right: 20px;
       border: solid 2px black;
       align: left;
       position: relative;
       border-left: 0px;
      }
      .block3 {
       margin-top: 35px;
       margin-left: auto;
       margin-right: -1.2em;
       width: 600px;
       background: #ffdc38;
       padding: 5px;
       padding-right: 20px;
       border: solid 2px black;
       border-right: 0px;

      }
      .block4 {
       margin-top: 30px;
       margin-left: 50px;
       margin-bottom: -5px;
       width: 540px;
       background: #ffffff;
       padding: 5px;
       padding-right: 20px;
       padding-bottom: 10px;
       border: solid 2px black;
       border-bottom: 0px;
       position: relative;
      }
      .block5{
      border: 0px;}

      .inside-text {
        font-weight: bold;

      }
      .inside-image{
        float: right;
      }

      #left { align: left; }
      #right { align: right; }
      #center { align: center; }"]]
       [:body
        [:div.block0
         [:div.block1 [:p#center "ДОГОВОР ЗАЙМА"]
          [:div.block-in  (str (:q26 (k @base)) "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;")]
          [:div.block-in (str "&nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;" (time-string) )]]
          [:div.block2
          [:ul {:style "list-style-type: none;"}
           [:li [:span.inside-text "Заёмщик:"] (str (:q0 (k @base)))]
           [:li [:span.inside-text "дата рождения:"] (str (:q1 (k @base)))]
           [:li [:span.inside-text "место рождения:"] (str (:q2 (k @base)))]
           [:li [:span.inside-text "гражданство:"] (str (:q3 (k @base)))]
           [:li [:span.inside-text "пол:"] (str (:q4 (k @base)))]
           [:li [:span.inside-text "паспорт (серия и номер):"] (str (:q5 (k @base)))]
           [:li [:span.inside-text "выдан:"] (str (:q6 (k @base)))]
           [:li [:span.inside-text "код подразделения:"] (str (:q7 (k @base)))]
           [:li [:span.inside-text "адрес регистрации:"] (str (:q8 (k @base)))]
           [:li [:span.inside-text "номер телефона:"] (str (:q9 (k @base)))]
           (when-not (= 0 (:q10 (k @base))) [:li [:span.inside-text "ИНН:"] (str (:q10 (k @base)))] )
           (when-not (= 0 (:q11 (k @base))) [:li [:span.inside-text "СНИЛС:"] (str (:q11 (k @base)))] )
           [:li [:span.inside-text "электронная почта:"] (str (:q12 (k @base)))]]]
         [:div [:img {:src "hands02.png" :alt "" :style "float: right;" :width "150"}]]
         [:div [:img.inside-image {:src "hands01.png" :alt "" :width "150"}]]
         [:div.block3
          [:ul {:style "list-style-type: none;"}
           [:li [:span.inside-text "Займодавец:"] (str (:q13 (k @base)))]
           [:li [:span.inside-text "дата рождения:"] (str (:q14 (k @base)))]
           [:li [:span.inside-text "место рождения:"]  (str (:q15 (k @base)))]
           [:li [:span.inside-text "гражданство:"] "РФ" (str (:q16 (k @base)))]
           [:li [:span.inside-text "пол:"] (str (:q17 (k @base)))]
           [:li [:span.inside-text "паспорт (серия и номер):"] (str (:q18 (k @base)))]
           [:li [:span.inside-text "выдан:"] (str (:q20 (k @base)))]
           [:li [:span.inside-text "код подразделения:"] (str (:q19 (k @base)))]
           [:li [:span.inside-text "адрес регистрации:"] (str (:q20 (k @base)))]
           [:li [:span.inside-text "номер телефона:"] (str (:q21 (k @base)))]
           [:li [:span.inside-text "электронная почта:"] (str (:q22 (k @base)))]]]
         [:div.block4
          [:div [:img {:src "hands03.png" :alt "" :style "float: left;" :width "150"}]]
          [:div [:img {:src "hands04.png" :alt "" :style "float: right;" :width "150"}]]
          [:p (str "Подписывая настоящий договор стороны признают, что на момент его подписания
          займодавец передал заёмщику, а заёмщик получил в долг от займодавца
           денежные средства в сумме " (str (:q23 (k @base)))  "на следующих условиях:")]
          [:ul {:style "list-style-type: none; list-style-position: inside; text-align: center; "}
           [:li [:span.inside-text "проценты:"] (if (= 0 (:q24 (k @base)))
                                                  "беспроцентный"
                                                  (str (:q24 (k @base)) "%") )]
           [:li [:span.inside-text "дата возврата:"] (if (= 0 (:q25 (k @base)))
                                                       "до востребования"
                                                       (str (:q25 (k @base))) )]]
          [:table.block5
           [:tr
            [:th "Заёмщик"]
            [:th "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"]
            [:th "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"]
            [:th "Займодавец"]] [:tr [:td "________________"]
                                 [:td "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"]
                                 [:td "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"]
                                 [:td "________________"]]]]]]] baos)
    (.toByteArray baos)))

(defn send-pdf-document
  ([token chat-id document] (send-pdf-document token chat-id {} document))
  ([token chat-id options document]
   (t/send-file token chat-id options document "/sendDocument" "document" "raspisonka.pdf")))

(h/defhandler handler
              (h/command-fn "start"
                            (fn [{{id :id name :first_name :as chat} :chat}]
                              (do (t/send-text token id (str "Привет, " name "! Расписонька- это бот  при помощи которого можно составить договор займа (долговую расписку) в простой письменной форме. Отвечай на вопросы до конца и Расписонька отправит тебе в чат заполненный и готовый для подписания pdf-документ, отвечающий всем требованиям закона. Вводить данные следует внимательно - если данные введены неправильно, то придётся начать заново (пока что так). Необходимы паспортные данные заёмщика и займодателя. Желательно знать ИНН и СНИЛС заёмщика (эти данные потребуются для подачи иска в суд, если деньги не будут возвращены)."))
                                  (t/send-text token id "/fast - Расписонька пришлёт документ для заполнения от руки \n /forgetme - удаление введенных данных \n /help - помощь \n (C)ode and design by {atticus legal design}.")
                                  (t/send-text token id "Вопрос 1/27.  Ф.И.О. заёмщика (того, кто берет в долг), например Сергеев Виктор Петрович")
                                  (send base assoc (keyword (str id)) {:step 0}))))

              (h/command-fn "help"
                            (fn [{{id :id :as chat} :chat}]
                              (t/send-text token id "/forgetme - Удаление введенных данных)\n /fast - Расписонька пришлёт документ для заполнения от руки. \n alexeevdev@yahoo.com - для вопросов, предложений и пожеланий. ")))

              (h/command-fn "forgetme"
                            (fn [{{id :id}  :chat :as message}]
                              (t/send-photo token id (io/file (io/resource "a.png")))))
              (h/command-fn "test"
                            (fn [{{id :id}  :chat :as message}]
                              (send-pdf-document token id (profile-pdf-stream-test)))))
              (h/message-fn
                (fn [{{id :id}  :chat :as message}]
                  (let [text (:text message)
                        k (keyword (str id))]
                    (when (not (or (= text "/forgetme") (= text "/start") (= text "/test")) )
                      (case (:step (k @base))
                        0 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                              (send base assoc-in [k :q0] text)
                              (t/send-text token id "Вопрос 2/27. Дата рождения заёмщика. Например: 11 февраля 1977 года или 11.07.1977"))
                        1 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                              (send base assoc-in [k :q1] text)
                              (t/send-text token id "Вопрос 3/27. Место рождения заёмщика. Например: город Ленинград, РСФСР"))
                        2 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                              (send base assoc-in [k :q2] text)
                              (t/send-text token id "Вопрос 4/27. Пол заёмщика. Например: М"))
                        3  (do (send base assoc-in [k :step] (inc (:step (k @base))))
                              (send base assoc-in [k :q3] text)
                              (t/send-text token id "Вопрос 5/27. Серия и номер паспорта. Например: 4005 733922"))
                        4  (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q4] text)
                               (t/send-text token id "Вопрос 6/27. Дата выдачи. Например: 16.03.2015 "))
                        5 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                              (send base assoc-in [k :q5] text)
                              (t/send-text token id "Вопрос 7/27. Кем выдан. Например: Ржевским ГУВД "))
                        6 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                              (send base assoc-in [k :q6] text)
                              (t/send-text token id "Вопрос 8/27. Код подразделения. Например: 730-722 "))
                        7 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                              (send base assoc-in [k :q7] text)
                              (t/send-text token id "Вопрос 9/27. Адрес регистрации. Например: Москва, Кремль "))
                        8 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                              (send base assoc-in [k :q8] text)
                              (t/send-text token id "Вопрос 10/27. Мобильный телефон. Например: +79367542235 "))
                        9 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                              (send base assoc-in [k :q9] text)
                              (t/send-text token id "Вопрос 11/27. ИНН заёмщика. Например: 760401693832. Если не знаете - поставьте значение 0 (ноль)."))
                        10 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q10] text)
                               (t/send-text token id "Вопрос 12/27. СНИЛС заёмщика. Например: 083 367 324 77. Если не знаете - поставьте значение 0 (ноль). "))
                        11 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q11] text)
                               (t/send-text token id "Вопрос 13/27. Электронная почта заемщика. Например: info@atticuslegaldesign.ru"))
                        12 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q12] text)
                               (t/send-text token id "Вопрос 14/27. Дата рождения займодателя. Например: 11 февраля 1977 года или 11.07.1977"))
                        13 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q13] text)
                               (t/send-text token id "Вопрос 15/27. Место рождения займодателя. Например: город Ленинград, РСФСР"))
                        14 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q14] text)
                               (t/send-text token id "Вопрос 16/27. Пол займодателя. Например: М"))
                        15  (do (send base assoc-in [k :step] (inc (:step (k @base))))
                                (send base assoc-in [k :q15] text)
                                (t/send-text token id "Вопрос 17/27. Серия и номер паспорта займодателя. Например: 4005 733922"))
                        16  (do (send base assoc-in [k :step] (inc (:step (k @base))))
                                (send base assoc-in [k :q16] text)
                                (t/send-text token id "Вопрос 18/27. Дата выдачи. Например: 16.03.2015 "))
                        17 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q17] text)
                               (t/send-text token id "Вопрос 19/27. Кем выдан. Например: Ржевским ГУВД "))
                        18 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q18] text)
                               (t/send-text token id "Вопрос 20/27. Код подразделения. Например: 730-722 "))
                        19 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q19] text)
                               (t/send-text token id "Вопрос 21/27. Адрес регистрации займодателя. Например: Москва, Кремль "))
                        20 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q20] text)
                               (t/send-text token id "Вопрос 22/27. Мобильный телефон займодателя. Например: +79367542235 "))
                        21 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q21] text)
                               (t/send-text token id "Вопрос 23/27. Электронная почта займодателя. Например: info@atticuslegaldesign.ru"))
                        22 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q22] text)
                               (t/send-text token id "Вопрос 24/27. Проценты по займу (только цифры, знак процента не нужен). Например: 6.75. Если займ беспроцентный - укажите 0. "))
                        23 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q23] text)
                               (t/send-text token id "Вопрос 25/27. Дата возврата. Например:  15.05.2021. Если займ до всотребования - укажите 0." ))
                        24 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q24] text)
                               (t/send-text token id "Вопрос 26/27. Введите сумму и валюту одалживаемых средств. Например: 300 000 рублей. "))
                        25 (do (send base assoc-in [k :step] (inc (:step (k @base))))
                               (send base assoc-in [k :q25] text)
                               (t/send-text token id "Вопрос 26/27. Введите место заключения сделки. Например: Санкт-Петербург . "))
                        26 (do (send base assoc-in [k :q26] text)
                               (t/send-text token id "Расписонька сейчас отправит готовый pdf документ. ")
                               (send-pdf-document token id (profile-pdf-stream id)))
                        )))))

(defn -main
  [& args]
  (when (str/blank? token)
    (println "Please provide token in TELEGRAM_TOKEN environment variable!")
    (System/exit 1))
  (<!! (p/start token handler)))