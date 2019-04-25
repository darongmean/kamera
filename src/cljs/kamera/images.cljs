(ns kamera.images
  (:require [reagent.core :as reagent]))

;; from https://www.w3schools.com/howto/howto_js_image_magnifier_glass.asp

(defn- cursor-pos [image event]
  (let [image-bounds (.getBoundingClientRect image)]
    {:x (- (.-pageX event)
           (.-left image-bounds)
           (.-pageXOffset js/window))
     :y (- (.-pageY event)
           (.-top image-bounds)
           (.-pageYOffset js/window))}))

(defn- ->magnifier-style [image glass event]
  (.preventDefault event)
  (let [{:keys [x y]} (cursor-pos image event)
        image-width (.-width image)
        image-height (.-height image)
        width-offset 50 ;;(/ (.-offsetWidth glass) 2)
        height-offset 50 ;;(/ (.-offsetHeight glass) 2)
        zoom 3
        border-width 3
        left (max (/ width-offset zoom)
                  (min x (- image-width (/ width-offset zoom))))
        top (max (/ height-offset zoom)
                 (min y (- image-height (/ height-offset zoom))))
        background-pos (str "-" (- (* left zoom) (- width-offset border-width)) "px "
                            "-" (- (* top zoom) (- height-offset border-width)) "px")]

    {:background-size (str (* image-width zoom) "px "
                           (* image-height zoom) "px")
     :left (- left (/ width-offset 2))
     :top (- top height-offset)
     :backgroundPosition background-pos}))

(defn image [url]
  (let [image-element (reagent/atom nil)
        show-glass? (reagent/atom false)
        glass-element (reagent/atom nil)
        magnifier-style
        (reagent/atom {:background-image (str "url('" url "')")
                       :background-repeat "no-repeat"})
        move-handler (fn [event]
                       (swap! magnifier-style
                              merge
                              (->magnifier-style @image-element
                                                 @glass-element
                                                 event)))]
    (reagent/create-class
     {:reagent-render
      (fn [url]
        [:a {:href url
             :target "_blank"}
         [:div.img-magnifier-container
          {:on-mouse-enter #(reset! show-glass? true)
           :on-mouse-leave #(reset! show-glass? false)}
          [:div.img-magnifier-glass
           {:class (when @show-glass? "visible")
            :style @magnifier-style
            :on-mouse-move move-handler}]
          [:img {:src url
                 :on-mouse-move move-handler}]]])

      :component-did-mount
      (fn [this]
        (let [dom-node (reagent/dom-node this)]
          (reset! image-element (.querySelector dom-node "img"))
          (reset! glass-element (.querySelector dom-node "div.img-magnifier-glass"))))})))
