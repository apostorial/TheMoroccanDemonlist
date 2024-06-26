from django.shortcuts import render, redirect, get_object_or_404
from .models import ClassicLevel, PlatformerLevel
from region.models import Region
from player.models import Player
from .forms import LevelSearchForm
from django.urls import reverse
from django.contrib import messages

# Create your views here.

def search(request):
    staff_members = Player.objects.filter(is_staff=True)
    if request.method == 'POST':
        form = LevelSearchForm(request.POST)
        if form.is_valid():
            query = form.cleaned_data['query']
            classic_levels = ClassicLevel.objects.filter(name__icontains=query)
            platformer_levels = PlatformerLevel.objects.filter(name__icontains=query)
            levels = list(classic_levels) + list(platformer_levels)
            if len(levels) == 1:
                level = levels[0]
                if isinstance(level, ClassicLevel):
                    return redirect(reverse('level:classic_level_detail', args=[level.id]))
                elif isinstance(level, PlatformerLevel):
                    return redirect(reverse('level:platformer_level_detail', args=[level.id]))
            elif len(levels) == 0:
                messages.error(request, ("No level found. Please try again."))
                return redirect("level:classic_mainlist")
            else:
                context = {
                    'levels': levels,
                    'staff_members': staff_members
                }
                return render(request, 'level/search.html', context)
        else:
            form = LevelSearchForm()
            return render(request, 'level/search.html', {'form': form, 'staff_members': staff_members})

def detail(request, pk):
    if ClassicLevel.objects.filter(pk=pk).exists():
        level = get_object_or_404(ClassicLevel, pk=pk)
        total_records = level.classiclevelrecord_set.count()
        perfect_records = level.classiclevelrecord_set.filter(record_percentage=100).count()
        youtube_id = level.youtube_link.split('.be/')[-1]
        staff_members = Player.objects.filter(is_staff=True)
        context = {
            'level': level,
            'total_records': total_records,
            'perfect_records': perfect_records,
            'youtube_id': youtube_id,
            'staff_members': staff_members
        }
        return render(request, 'level/classic/detail.html', context)
    elif PlatformerLevel.objects.filter(pk=pk).exists():
        level = get_object_or_404(PlatformerLevel, pk=pk)
        total_records = level.platformerlevelrecord_set.count()
        youtube_id = level.youtube_link.split('.be/')[-1]
        staff_members = Player.objects.filter(is_staff=True)
        context = {
            'level': level,
            'total_records': total_records,
            'youtube_id': youtube_id,
            'staff_members': staff_members
        }
        return render(request, 'level/platformer/detail.html', context)

def classic_mainlist(request):
    main_levels = ClassicLevel.objects.filter(ranking__lte=75)
    staff_members = Player.objects.filter(is_staff=True)
    context = {
        'main_levels': main_levels,
        'staff_members': staff_members
    }
    return render(request, 'level/classic/mainlist.html', context)

def classic_extendedlist(request):
    extended_levels = ClassicLevel.objects.filter(ranking__range=(76, 150))
    staff_members = Player.objects.filter(is_staff=True)
    context = {
        'extended_levels': extended_levels,
        'staff_members': staff_members
    }
    return render(request, 'level/classic/extendedlist.html', context)

def classic_legacylist(request):
    legacy_levels = ClassicLevel.objects.filter(ranking__gt=150)
    staff_members = Player.objects.filter(is_staff=True)
    context = {
        'legacy_levels': legacy_levels,
        'staff_members': staff_members
    }
    return render(request, 'level/classic/legacylist.html', context)

def classic_stat_viewer(request):
    regions = Region.objects.all()
    for region in regions:
        region.players = Player.objects.filter(region=region)
    return render(request, 'level/classic/statviewer.html', {'regions': regions})

def platformer_mainlist(request):
    main_levels = PlatformerLevel.objects.filter(ranking__lte=75)
    staff_members = Player.objects.filter(is_staff=True)
    context = {
        'main_levels': main_levels,
        'staff_members': staff_members
    }
    return render(request, 'level/platformer/mainlist.html', context)

def platformer_stat_viewer(request):
    regions = Region.objects.all()
    for region in regions:
        region.players = Player.objects.filter(region=region)
    return render(request, 'level/platformer/statviewer.html', {'regions': regions})